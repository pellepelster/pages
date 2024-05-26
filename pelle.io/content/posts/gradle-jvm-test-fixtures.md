---
title: "Providing test fixtures in Gradle projects"
date: 2024-05-26T12:00:00+01:00
tags: [ "jvm", "gradle", "test" ]
---

Providing test utility classes or test fixtures for other projects in Gradle environments can be an annoying and cumbersome endeavor. Often the solutions involve the creation of extra projects to provide the needed testing functionality for other modules or projects, or include manipulation of Gradle source sets and configurations. This post shows how to achieve this using functionality that is already included in Gradle and Junit.

<!--more-->

## The problem(s)

Let's assume a typical multimodule Gradle setup, with a module containing all code needed to access the data model called `model` and a module containing the application itself called `application`

```shell
gradle-jvm-test-fixtures/
├─ application/
│  ├─ src
│  │  ├─ main/...
│  │  └─ test/...
│  └─ build.gradle.kts
├─ model/
│  │  ├─ main/...
│  │  └─ test/...
│  └─ build.gradle.kts
├─ [...]
└─ settings.gradle.kts
```

For the sake of simplicity and brevity of this example, the application is a simple todo list manager that can manage groups of users, where each user can have a number of todo items.

Looking at the `model` module we can see the unit tests for the `TodoItemsRepository` are already quite involved in terms of setup needed to test the implementation. 

**model/src/test/kotlin/io/pelle/kitchensink/model/TodoItemsRepositoryTest.kt**
```kotlin
class TodoItemsRepositoryTest {
    @Test
    fun testListTodoItems() {
        val todoItemsRepository = TodoItemsRepository()
        val usersRepository = UsersRepository(todoItemsRepository)
        val groupsRepository = GroupsRepository(usersRepository)

        val group1 = groupsRepository.create("group1")
        val user1 = usersRepository.create("user1", group1)

        todoItemsRepository.list() shouldBe emptyList()
        todoItemsRepository.create("item1", false, user1)

        assertSoftly(todoItemsRepository.list()) {
            it shouldHaveSize 1
            it[0].name shouldBe "item1"
        }
    }
}
```

While this is perfectly ok for the `model` module itself, it creates an issue for the `application` module that builds services based on the basic primitives from the `model` module. For example the `TodoService` provides functionality that aggregates information using multiple repositories from the `model` module. 

**application/src/main/kotlin/io/pelle/kitchensink/app/TodoService.kt**
```kotlin
class TodoService(
    private val groupsRepository: GroupsRepository,
    private val usersRepository: UsersRepository,
    private val todoItemsRepository: TodoItemsRepository
) {

    fun totalOpenTodos(name: String) =
        groupsRepository.forName(name)?.let { group ->
            usersRepository.forGroup(group.id).sumOf {
                todoItemsRepository.forUser(it.id).filter { !it.done }.size
            }
        }
}
```

Being a responsible developer, of course we also want to unit test the `TodoService` which confronts us with two problems

#### Problem A

For Java/Kotlin modules and projects, the code from the `main` [source set](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.SourceSet.html) `model/src/main/...` automatically exposes two configurations (`implementation` and `api` ) that can be used in other modules like this

**application/build.gradle.kts**
```groovy
dependencies {
    implementation(project(":model"))

    // [...]
}
```

Unfortunately, this approach does not work for test sources, so we can not re-use the code from `model/src/test/...` as a dependency in the `application` project. While this is inconvenient, it also protects us from creating accidental dependencies, since we don't want other modules to start building up on our testing internals.

#### Problem B

The whole testbed setup needed is complicated and also would make tests from dependent modules convoluted and hard to read. It also creates a maintenance issue, since changes and refactorings would need to tickle down into other projects and could cause local problems in the respective tests using the testbed setup. 


## The solution

The solution for **problem A** is a Gradle plugin, that is already baked into all current Gradle distributions called [`java-test-fixtures`](https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures). This plugin adds a new source set to the project called `testFixtures`

```shell
gradle-jvm-test-fixtures/
├─ application/
│  ├─ ...
├─ model/
│  │  ├─ main/...
│  │  └─ test/...
│  │  └─ testFixtures/...
│  └─ build.gradle.kts
└─ [...]
```

This source set (like the `main`) source set also creates two configuration that are automatically exposed as `implementation` and `api`. So all code from `src/testFixtures` can be used in other projects like this

**application/build.gradle.kts**
```groovy
dependencies {
    testImplementation(testFixtures(project(":model")))

    // [...]
}
```

Note the subtle differences here, we are declaring a dependency for our test code with `testImplementation` explicitly on the `testFixtures` configuration exported by the `model` module.


**Problem B** be also can be solved with something that is already included in most Gradle project setups. Since major version 5 junit comes with a rich extension API, that we can use to inject the testbed setup into other test functions. This lets us nicely encapsulate the test setup and also creates a defined API for other projects to use in their test code.  

First step here is to encapsulate the testbed setup in a class, providing typical test bed scenarios that can be created with simple function calls.

**model/src/testFixtures/kotlin/io/pelle/kitchensink/model/ModelTestBed.kt**
```kotlin
public class ModelTestBed {
    val todoItemsRepository = TodoItemsRepository()
    val usersRepository = UsersRepository(todoItemsRepository)
    val groupsRepository = GroupsRepository(usersRepository)

    data class TestBed(val group: Group, val user: User, val todoItem: TodoItem)

    fun createUserWithOpenTodoItem(): TestBed {
        val group = groupsRepository.create("group1")
        val user = usersRepository.create("user1", group)
        val todoItem = todoItemsRepository.create("item1", false, user)

        return TestBed(group, user, todoItem)
    }

    fun createUserWithDoneTodoItem(): TestBed {
        val group = groupsRepository.create("group2")
        val user = usersRepository.create("user2", group)
        val todoItem = todoItemsRepository.create("item2", true, user)

        return TestBed(group, user, todoItem)
    }
}
```

Now we can define a junit `extension` of the type `ParameterResolver`

**model/src/testFixtures/kotlin/io/pelle/kitchensink/model/ModelTestBedExtension.kt**
```kotlin
public class ModelTestBedExtension : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        parameterContext.parameter.type.isAssignableFrom(ModelTestBed::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        ModelTestBed()
}
```

which for our case supports the injection of `ModelTestBed` parameters. Coming back to our initial issue on writing a unit test for the `TodoService`, we can now extend the unit test with `@ExtendWith(ModelTestBedExtension::class)` telling junit to use our parameter resolver for all tests.

**application/src/test/kotlin/io/pelle/kitchensink/app/TodoServiceTest.kt**
```kotlin
@ExtendWith(ModelTestBedExtension::class)
class TodoServiceTest {

    @Test
    fun testCountOpenTodos(modelTestBed: ModelTestBed) {

        val testBed1 = modelTestBed.createUserWithOpenTodoItem()
        val testBed2 = modelTestBed.createUserWithDoneTodoItem()

        val service =
            TodoService(modelTestBed.groupsRepository, modelTestBed.usersRepository, modelTestBed.todoItemsRepository)

        service.totalOpenTodos(testBed1.group.name) shouldBe 1
        service.totalOpenTodos(testBed2.group.name) shouldBe 0
    }

}
```

Then we can use the `ModelTestBed` as a parameter, and Junit will take care of injecting it into the test methods, where we then can use the testbed functions `createUserWithOpenTodoItem` and `createUserWithDoneTodoItem` depending on our needs.

This can even be used cross-projects, since `java-test-fixtures` also automatically add a publishing configuration for your modules. All modules using `java-test-fixtures` will then automatically also publish the `testFixures` as artifacts with the postfix `test-fixtures`.

> If you want to experiment on your own, as always the full source code is available [here](https://github.com/pellepelster/kitchen-sink/tree/master/gradle-jvm-test-fixtures)