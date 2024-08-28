---
title: "Infrastructure testing with Solidblocks"
date: 2024-08-26T12:00:00+01:00
tags: [ "solidblocks", "test" ]
---

As time goes by and a project grows, ideas and concepts that initially seemed like good and pragmatic solutions can sometimes deteriorate into a convoluted mess. Recently, implementing a small feature in the [Solidblocks infrastructure suite](https://github.com/pellepelster/solidblocks) went from a pleasant Friday afternoon coding session to an integration testing nightmare, caused by an overabundance of infrastructure testing approaches. 

This post will highlight the different approaches and offer a streamlined solution that will work indefinitely (until it doesn't :-)).
<!--more-->

## The problem(s)

Currently, three different types of integration tests are implemented in the [Solidblocks components](https://pellepelster.github.io/solidblocks/#components). The type depends on the implementation language and the time of component creation.

## Shell

The oldest and most basic components are written in shell script, which while often not the best solution, sometimes is a necessity because nothing else is available. Tests for those components are also written in Bash and while the [smaller](https://github.com/pellepelster/solidblocks/blob/c0cf1180f1a6666f075220baff73a71020b9a937/solidblocks-shell/test/unit/test_download.sh) testcases are kind of readable, the "[testing framework](https://github.com/pellepelster/solidblocks/blob/c0cf1180f1a6666f075220baff73a71020b9a937/solidblocks-shell/lib/test.sh)" is a homegrown and half-baked solution.  Larger more complex cases like the `test_*` tasks for the [Hetzner RDS module](https://github.com/pellepelster/solidblocks/blob/c0cf1180f1a6666f075220baff73a71020b9a937/solidblocks-hetzner/do) have become very hard to maintain and are nearly unreadable.

#### Pros
* Easy to work with, runs everywhere
* Naturally integrates well with everything related to the operating system

#### Cons
* Difficult to debug; breakpoints and variable introspection need to be emulated with `read` and `echo`
* Maintainability and readability degrade quickly when scripts become larger
* No standardized test frameworks; every aspect needs to be handcrafted
* Integration with other APIs is only feasible through CLI commands or custom-crafted cURL calls

## Python

While the components initially written in Python have been replaced or rewritten over time, some [pytest-testinfra](https://testinfra.readthedocs.io/en/latest/) tests still remain. Given that testinfra aims to test the deployed servers' state, it aligns closely with Solidblocks core functionality. For more details, please see this [post](/posts/infrastructure-testing-testinfra/).

#### Pros
* Unlike bash, it's a "real" programming language.
* Can be configured to support test reporting formats like JUnit's XML format, which is understood by most continuous integration systems.
* Features an ecosystem with robust support for most external APIs (cloud providers, etc.) However, the SDKs for more niche applications tend to become outdated quickly.

#### Cons
* Pytest can be challenging to extend and only provides a rudimentary set of assertions.
* Running tests in parallel has proven to be unstable and doesn't appear to be [currently maintained](https://github.com/kevlened/pytest-parallel/issues/104#issuecomment-1293941066), leading to prolonged test run times.
* As a personal preference, since Python is a dynamically typed language, it makes refactoring and maintenance slightly more difficult than they should be, even with full IDE support.

## Kotlin

Based on JUnit 5 (Jupiter) and Kotlin some more complex integration tests have evolved over time, mainly for the [PostgreSQL RDS](https://pellepelster.github.io/solidblocks/rds/index.html) module.  These tests, which could benefit from some further refinement, have nonetheless proved easy to [read and maintain](https://github.com/pellepelster/solidblocks/blob/c7b6fd5470eabea67cade320d58ea319855d9838/solidblocks-rds-postgresql/test/src/test/kotlin/de/solidblocks/rds/postgresql/test/RdsPostgresqlConfigurationTest.kt#L210) over the years.

#### Pros
* It's a "real" programming language.
* [Kotest](https://kotest.io/) offers a rich set of assertions that can be used or easily extended.
* Offers an ecosystem with good support for most external APIs (e.g. cloud providers, etc.).
* Makes it easy to develop expressive, DSL-like library functions for integration tests.
* Produces reports in JUnit's XML format which is understood by most continuous integration systems.
* Support for parallel execution comes out of the box.

#### Cons
* It's significantly heavier than Python and Bash due to the reliance on JVM and Maven/Gradle.
* It may feel unnatural to develop infrastructure-related code in Kotlin.
## Potential solutions(s)

As a proof-of-concept,  and learning from the experiences implementing the integration test for the [shell component](https://pellepelster.github.io/solidblocks/shell/index.html), I plan to replace them with Kotlin and JUnit using [Kotest](https://kotest.io/) assertions. Based on my experience with the current shell integration tests, the new solution should satisfy the following minimum requirements:

* Support set up of repeatable testbeds for running commands, including setup for files, folders, and environment variables.
* Provide an expressive way to assert state and outcome of tests during and after test execution. This includes assertions for the state of files and folders as well as the exit code/runtime and log output of commands.
* Support debugging with breakpoints and introspection of current state at the breakpoint
* Provide detailed yet concise logging of test setup, execution, and teardown.
* Automate the cleanup of leftover resources after test execution.
* Support testing of specific shell functions in isolation.

## Files and Folders

Building up on the already powerful [Kotlin path API](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io.path/) and extending the basic [Kotest path and file assertions](https://kotest.io/docs/assertions/core-matchers.html) a DSL-like API to create and assert files and folders could look like this

**files and folders example**

```kotlin
val tempDir = tempDir()

// set up testbed by creating files from various sources
tempDir.file("file1.txt").content("some file1 content").create()
tempDir.fileFromResource("snippets/file-from-classpath.txt").create()
tempDir
    .fileFromPath(workingDir().resolve("src/test/resources/snippets/file-from-path.txt"))
    .create()
tempDir
    .zipFile("test1.zip")
    .entry("file2.txt", "some file2 content")
    .entry("file3.txt", "some file3 content")
    .create()

// assert testbed is ready to go
tempDir shouldContainNFiles 4

// call some code working on the testbed
reverseFile1Content(tempDir.path)
deleteTest1Zip(tempDir.path)
createFileWithUnpredictableName(tempDir.path)

// assert result of "business" code
tempDir shouldContainNFiles 4
tempDir singleFile ("file1.txt") shouldHaveContent "content file1 some"
tempDir singleFile
        ("file1.txt") shouldHaveChecksum
        "46cf7a4ae492a815c35a5a17395fee774f2fb2811ec3015b7c64b98a6238077a"
tempDir.singleFile("test1.zip").shouldNotExist()
tempDir.matchSingleFile(".*unpredictable_.*") shouldHaveContent "unpredictable file content"

// remove all files for another test
tempDir.clean()

// call some code working on the testbed
createFileWithUnpredictableName(tempDir.path)

// assert result again
tempDir shouldContainNFiles 1
tempDir matchSingleFile (".*unpredictable_.*") shouldHaveContent "unpredictable file content"

// remove temporary directory
tempDir.close()
```

[full sourcecode](https://github.com/pellepelster/solidblocks/blob/main/solidblocks-test/src/test/kotlin/de/solidblocks/infra/test/snippets/FilesAndFoldersSnippet.kt)

Running this example produces a nice and hopefully readable output detailing what the testbed setup is doing. This is vital during debugging issues in Continuous Integration (CI) environments where it might not be possible to set breakpoints for bug detection.

**example log output**

```
000.002s [  test] created directory '/tmp/test1901788291164134911'
000.003s [  test] created file '/tmp/test1901788291164134911/file1.txt' with size 18
000.000s [  test] created file '/tmp/test1901788291164134911/file-from-classpath.txt' with size 27
000.001s [  test] created file '/tmp/test1901788291164134911/file-from-path.txt' with size 22
000.011s [  test] created '/tmp/test1901788291164134911/test1.zip' with 2 entries (file2.txt, file3.txt)
reverseFile1Content was called
deleteTest1Zip was called
createFileWithUnpredictableName was called
000.047s [  test] deleting content of directory '/tmp/test1901788291164134911'
000.048s [  test] deleting  '/tmp/test1901788291164134911/unpredictable_7e8a5fbc-cf25-4439-88df-cad46185829a.txt'
000.048s [  test] deleting  '/tmp/test1901788291164134911/file-from-classpath.txt'
000.049s [  test] deleting  '/tmp/test1901788291164134911/file1.txt'
000.049s [  test] deleting  '/tmp/test1901788291164134911/file-from-path.txt'
createFileWithUnpredictableName was called
000.050s [  test] deleting directory '/tmp/test1901788291164134911'
```

## Resource cleanup

As you can see at the end of the example, we manually need to call `close()` on the created `tempDir` to remove the temporary files and folders inside it, as well as the `tempDir` itself. As a general pattern all resource-creating helpers implement the `java.io.Closeable` interface and will clean up intermediary resources when called. To make this easier to handle and avoid accidentally littering the system when executing tests, a JUnit test-extension is available that can inject a context which can be used to create test resources. Resources created by this context will be auto-closed when the test method is finished

**auto-close example**

```kotlin
@ExtendWith(SolidblocksTest::class)
public class AutoCloseSnippets {

    @Test
    fun autoCloseSnippet(testContext: SolidblocksTestContext) {
        // `tempDir` created from `SolidblocksTestContext` will be
        // auto-deleted when `snippet`is finished
        val tempDir = testContext.createTempDir()

        tempDir.file("some-file.txt").content("some-content").create()
    }
}
```

[full sourcecode](https://github.com/pellepelster/solidblocks/blob/main/solidblocks-test/src/test/kotlin/de/solidblocks/infra/test/snippets/AutoCloseSnippets.kt)

## Running commands

Now that we can create testbeds containing files and folders, the next step is to run commands on these testbeds. Again, the test context allows us to run arbitrary commands on our local machine using the `local()` method.

**example local command**

```kotlin
@Test
fun localCommandSnippet(testContext: SolidblocksTestContext) {
    val currentUserName = System.getProperty("user.name")
    val result = testContext.local().command("whoami").runResult()

    result shouldHaveExitCode 0
    result outputShouldMatch (".*$currentUserName.*")
    result.stderrShouldBeEmpty()
}
```

[full sourcecode](https://github.com/pellepelster/solidblocks/blob/main/solidblocks-test/src/test/kotlin/de/solidblocks/infra/test/snippets/CommandSnippets.kt)

While running the command locally is a good start, I also want to make sure my command under test can also can be run in other environments. Let's say we want to make sure it also works on an Ubuntu 20.04. system. `SolidblocksTestContext` also can help with that, as it allows us to run commands in docker containers, while allowing the same assertions that we can use for `local()`

**example docker command**

```kotlin
  @Test
fun dockerCommandSnippet(testContext: SolidblocksTestContext) {
    val result = testContext.docker(DockerTestImage.UBUNTU_22).command("whoami").runResult()

    result shouldHaveExitCode 0
    result outputShouldMatch (".*root.*")
    result.stderrShouldBeEmpty()
}
```

[full sourcecode](https://github.com/pellepelster/solidblocks/blob/main/solidblocks-test/src/test/kotlin/de/solidblocks/infra/test/snippets/CommandSnippets.kt)

We can take this approach even further, ensuring that the command works across a broad range of environments.

**example multiple docker commands**

```kotlin
  @Test
fun dockerCommandsSnippet(testContext: SolidblocksTestContext) {
    listOf(
        DockerTestImage.UBUNTU_20,
        DockerTestImage.UBUNTU_22,
        DockerTestImage.UBUNTU_24,
        DockerTestImage.DEBIAN_11,
        DockerTestImage.DEBIAN_11,
        DockerTestImage.DEBIAN_12,
    )
        .forEach {
            val result = testContext.docker(it).command("whoami").runResult()

            result shouldHaveExitCode 0
            result outputShouldMatch (".*root.*")
            result.stderrShouldBeEmpty()
        }
}
```

[full sourcecode](https://github.com/pellepelster/solidblocks/blob/main/solidblocks-test/src/test/kotlin/de/solidblocks/infra/test/snippets/CommandSnippets.kt)

## Asserting complex commands

Sometimes I do not only want to assert the end result of a command call, but also make sure certain other things also happened during execution. `local()` as well as `docker(image)` command calls allow us to register callbacks via `assert` that can be used to assert state during execution.

**example assertion steps**

```kotlin
@Test
fun longRunningCommandSnippet(testContext: SolidblocksTestContext) {
    val longRunningCommand =
        """
      #!/usr/bin/env bash
      set -eu -o pipefail
      
      sleep 2
      echo "something has happened"

      sleep 2
      echo "something else has happened"

      sleep 2
      echo "everything worked"
      """
            .trimIndent()

    val tempDir = testContext.createTempDir()
    val command =
        tempDir.file("long-running-script.sh").content(longRunningCommand).executable().create()

    val result =
        testContext
            .local()
            .command(command)
            .assert { it.waitForOutput(".*something has happened.*") }
            .assert { it.waitForOutput(".*something else has happened.*") }
            .runResult()

    result shouldHaveExitCode 0
    result stdoutShouldMatch ".*everything worked.*"
    result.stderrShouldBeEmpty()
    result runtimeShouldBeLessThan 8.seconds
}
```

This is also possible in a linear fashion, for cases where nesting callback may obfuscate the test, look [here](https://github.com/pellepelster/solidblocks/blob/main/solidblocks-test/src/test/kotlin/de/solidblocks/infra/test/snippets/CommandSnippets.kt) for more examples.

For even more complex scenarios, those `waitForOutput` callbacks also allow us to respond to executed commands. Assuming we have a script that waits for some interactive input, we can provide it as follows:


**example responds**

```kotlin
@Test
fun respondToCommandSnippet(testContext: SolidblocksTestContext) {
    val respondToCommand =
        """
      #!/usr/bin/env bash
      set -eu -o pipefail
      
      echo "please enter name"
      read

      echo "name was entered"
      """
            .trimIndent()

    val tempDir = testContext.createTempDir()
    val command =
        tempDir.file("respond-to-command.sh").content(respondToCommand).executable().create()

    val result =
        testContext
            .local()
            .command(command)
            .assert { it.waitForOutput(".*please enter name.*") { "Steve McQueen" } }
            .assert { it.waitForOutput(".*name was entered.*") }
            .runResult()

    result shouldHaveExitCode 0
}
```

## Scripts

Being able to test single commands is great, unfortunately a big part
of [Solidblocks shells](https://pellepelster.github.io/solidblocks/shell/index.html) functionality are shell functions that can be included in other scripts. Ideally akin to a typical unit test I would like to test those distinct functions without the need to create a script for each test case. The `script()` function allows to declaratively create a script that will execute those functions step by step. Between each step there will be a wait to allow for assertions to be executed before continuing with the next step. Internally this is achieved by creating a script with `read` commands between steps, that are handled by `waitForOutput`s that wait until `finished step <number>` appears and only continues when the assertions are done.

**script generator overview**

```
 ┌─────────────────────────┐
 │       library1.sh       ┼─────┐
 ───────────────────────────     │
 │ some_function() {       │     │
 │   echo "hello world"    │     │     ┌──────────────────────────────────────────────┐
 │ }                       │     │     │  .script()                                   │
 └─────────────────────────┘     └────►│  .includes("library1.sh")                    │
                                 ┌────►│  .includes("library2.sh")                    │
                                 │     │  .step("some_function arg1") {               │
 ┌─────────────────────────┐     │     │      it.waitForOutput(".*hello world.*")     │
 │       library2.sh       ├─────┘     │  }.step("another_function arg1") {           │
 ───────────────────────────           │      it.waitForOutput(".*hello universe.*")  │
 │ another_function() {    │           │  }.run()                                     │
 │   echo "hello universe" │           └───────────────────────┬──────────────────────┘
 │ }                       │                                   │
 └─────────────────────────┘                                   │
                                                               ▼
                                                  ┌─────────────────────────┐
                                                  │        script.sh        │
                                                  ───────────────────────────
                                                  │ source library1.sh      │
                                                  │ source library2.sh      │
                                                  │                         │
                                                  │ echo "step 0"           │
                                                  │ some_function arg1      │
                                                  │ echo "finished step 0"  │
                                                  │ read                    │
                                                  │                         │
                                                  │ echo "step 1"           │
                                                  │ another_function arg2   │
                                                  │ echo "finished step 1"  │
                                                  │ read                    │
                                                  └─────────────────────────┘
```

**script generator example**

```kotlin
  @Test
fun scriptSnippet(testContext: SolidblocksTestContext) {
    val tempDir = testContext.createTempDir()

    val library1 =
        """
        #!/usr/bin/env bash
        some_function() {
            echo "hello world"
        }
        """
            .trimIndent()
    val library1File = tempDir.file("library1.sh").content(library1).executable().create()

    val library2 =
        """
        #!/usr/bin/env bash
        another_function() {
            echo "hello universe"
        }
        """
            .trimIndent()
    val library2File = tempDir.file("library2.sh").content(library2).executable().create()

    val result =
        testContext
            .local()
            .script()
            .includes(library1File)
            .includes(library2File)
            .step("some_function arg1") { it.waitForOutput(".*hello world.*") }
            .step("another_function arg2") { it.waitForOutput(".*hello universe.*") }
            .run()

    result shouldHaveExitCode 0
}
```

## Current state and roadmap

For now the PoC proved, that replacing the Bash based tests ist feasible, have a
look [here](https://github.com/pellepelster/solidblocks/tree/main/solidblocks-shell/src/test/kotlin/de/solidblocks/shell/test) for the actual integration tests. If you want to try it out for yourself, solidblocks-test is available at [Maven central](https://central.sonatype.com/artifact/de.solidblocks/infra-test) have a look at the [docs](https://pellepelster.github.io/solidblocks/test/) for more info or [here](https://github.com/pellepelster/solidblocks/tree/main/solidblocks-test/snippets/solidblocks-test-gradle) for an example project.

The next step will be to replace the cloud based integration tests for the Terraform modules opening up the possibility to test Terraform modules from Kotlin JUnit.