---
title: "Infrastructure testing with Solidblocks"
date: 2024-08-26T12:00:00+01:00
tags: [ "solidblocks", "test" ]
draft: true
---

As time goes by and a projects grows, ideas and concepts that initially looked like a good and pragmatic solution
sometimes deteriorate a convoluted mess.

Recently implementing a small feature in
the [Solidblocks infrastructure suite](https://github.com/pellepelster/solidblocks) went from a nice Friday afternoon
coding session into an integration testing nightmare, caused by too many infrastructure testing approaches. This post
will highlight the different approaches and offer a streamlined solution.

<!--more-->

## The problem(s)

Depending on the implementation language of the various [Solidblocks components](https://pellepelster.github.io/solidblocks/#components) and its age, currently three different kind of integration tests are implemented

## Shell

The oldest and most basic components are written in shell script, which while often not the best solution, sometimes is a necessity because nothing else is available. Tests for those components are also written in Bash and while the [smaller](https://github.com/pellepelster/solidblocks/blob/c0cf1180f1a6666f075220baff73a71020b9a937/solidblocks-shell/test/unit/test_download.sh) testcases are kind of readable, the "[testing framework](https://github.com/pellepelster/solidblocks/blob/c0cf1180f1a6666f075220baff73a71020b9a937/solidblocks-shell/lib/test.sh)" is a only a homegrown and half-baked solution. Larger more complex cases like the `test_*` tasks for the [Hetzner RDS moduel](https://github.com/pellepelster/solidblocks/blob/c0cf1180f1a6666f075220baff73a71020b9a937/solidblocks-hetzner/do) have become very hard to maintain.

#### pro

* easy to work with, runs everywhere
* works naturally well with everything operating system related

#### cons

* hard to debug mostly, breakpoints and variable introspection need to be emulated with `read` and `echo`
* maintainability and readability rapidly degrades when scripts become larger
* no standardized test frameworks, everything needs to be handcrafted
* integration with other APIs os only feasible with CLI commands or custom crafted curls calls 

## Python

While the components written in Python have been replaced and/or rewritten in the meantime, some [pytest-testinfra](https://testinfra.readthedocs.io/en/latest/) tests still remain. With testinfra aiming at testing the state of servers, its already very close to the core of what Solidblocks does, see also this [post](/posts/infrastructure-testing-testinfra/) for more details. 

#### pro

* opposed to bash a "real" programing language
* can be configured to support test reporting formats like Junit's XML format which is understood by most continuous integrations systems
* ecosystem with good support for most external APIs (cloud provider, etc) also the SDKs for more niche applications tend to get stale very fast

#### cons

* pytest can be awkward to extend and has only a very basic set of assertions
* running tests in parallel has been very unstable and also does not seem to be [maintained currently](https://github.com/kevlened/pytest-parallel/issues/104#issuecomment-1293941066)
* this might be a personal preference but since Python is a dynamically typed language this makes refactorings and maintenance a little bit harder than it should be, even with full IDE support. 

## Kotlin

Based on Junit 5 and Kotlin some more complex integration tests have evolved over time, mainly for the [PostgreSQL RDS](https://pellepelster.github.io/solidblocks/rds/index.html) module, which while having some room for improvement have proved to be easy to [read and maintain](https://github.com/pellepelster/solidblocks/blob/c7b6fd5470eabea67cade320d58ea319855d9838/solidblocks-rds-postgresql/test/src/test/kotlin/de/solidblocks/rds/postgresql/test/RdsPostgresqlConfigurationTest.kt#L210) over the years.

#### pro

* again "real" programing language
* [kotest](https://kotest.io/) offers a rich set of assertions that can be used, or easily extended
* ecosystem with good support for most external APIs (cloud provider, etc)
* easy to develop an expressive, DSL like library functions for integration tests
* Junit's XML format which is understood by most continuous integrations systems

#### cons

* compared to Python and Bash way more heavy due to JVM and Maven/Gradle
* it "feels" unnatural do develop infrastructure related code in Kotlin

## Potential solutions(s)

Based on the experiences from implementing the integration tests for the [shell component](https://pellepelster.github.io/solidblocks/shell/index.html) we will use this as a proof of concept to come up with a nice clean solution using Kotlin and Junit using Lotest assertions. From our experience the new solution should at least fulfill the following requirements

* support debugging with breakpoints and introspection of current state at the breakpoint
* concise logging of test setup, execution and teardown
* automatic cleanup of leftover resources after test execution
* support set up of repeatable testbeds
  * files and folders
  * environment variables
* after command execution an expressive way to
  * assert state of files and folders
  * assert exit code/runtime and log output of commands

## Files and Folders

By using the already powerful [Kotlin path API](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io.path/) and extending the basic [Kotest path and file assertions](https://kotest.io/docs/assertions/core-matchers.html) we can create a fluent and powerful API to create and assert files and folders

**example**
// TODO link
```kotlin
val tempDir = tempDir()

// set up testbed by creating files from various sources
tempDir.file("file1.txt").content("some file1 content").create()
tempDir.fileFromResource("snippets/file-from-classpath.txt").create()
tempDir.fileFromPath(workingDir().resolve("src/test/resources/snippets/file-from-path.txt")).create()
tempDir.zipFile("test1.zip")
  .entry("file2.txt", "some file2 content")
  .entry("file3.txt", "some file3 content").create()

// assert testbed is ready to go
tempDir shouldContainNFiles 4

// call some code working on the testbed
reverseFile1Content(tempDir.path)
deleteTest1Zip(tempDir.path)
createFileWithUnpredictableName(tempDir.path)

// assert result of "business" code
tempDir shouldContainNFiles 4
tempDir singleFile("file1.txt") shouldHaveContent "content file1 some"
tempDir singleFile("file1.txt") shouldHaveChecksum "46cf7a4ae492a815c35a5a17395fee774f2fb2811ec3015b7c64b98a6238077a"
tempDir.singleFile("test1.zip").shouldNotExist()
tempDir.matchSingleFile(".*unpredictable_.*") shouldHaveContent "unpredictable file content"

// remove all files for another test
tempDir.clean()

// call some code working on the testbed
createFileWithUnpredictableName(tempDir.path)

// assert result again
tempDir shouldContainNFiles 1
tempDir matchSingleFile(".*unpredictable_.*") shouldHaveContent "unpredictable file content"

// remove temporary directory
tempDir.remove()
```

running this example we can also see that we get clear output what the testbed setup is doing, which is espially helpful when debugging issues in CI where we might not have the luxury of setting breakpoints to debug bugs

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