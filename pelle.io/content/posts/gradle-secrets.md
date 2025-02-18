---
title: "Handling secrets in Gradle"
date: 2025-02-18T07:00:00+01:00
---

If you are ready for another instance of me rambling about developer experience, this post will detail a Gradle pattern to ensure that secrets for Java or Kotlin integration tests are readily available locally and in CI, without the need to manually set up the development environment.

<!--more-->

When your service needs to talk to any third party system to fulfil its purpose, sooner or later you might be confronted with the issue that you need to use some secrets like API tokens or user credentials in your integration test. 

To keep developer friction as low as possible the solution should ensure that

* Only encrypted secrets are stored in the source repository
* Tests should be runnable directly from the IDE without the need to manually fiddle with the test config
* The mechanism should allow for secrets to be injected via environment variables as well to support CI/CD systems that might not have access to the secret encryption. The reasoning here is that CI/CD systems are a very juicy and vulnerable target for supply chain attacks, so having some mechanism there that is able to decrypt all our secrets is likely not a good idea 

> For the encryption part we will use [pass](https://www.passwordstore.org/) which is a little clunky to set up due to its GPG heritage, but integrates nicely with other tooling and will most likely still work when the sun explodes. The setup is not part of this post. Of course a ton of other options are possible like the [1password cli](https://developer.1password.com/docs/cli/get-started/) or the [AWS secrets manager cli](https://docs.aws.amazon.com/zh_cn/cli/latest/reference/secretsmanager/index.html)

## Overview

To integrate data from external sources Gradle provides the [ValueSource](https://docs.gradle.org/current/kotlin-dsl/gradle/org.gradle.api.provider/-value-source/index.html) interface which encapsulates the retrieval process in a way, so that the [configuration phase](https://docs.gradle.org/current/userguide/build_lifecycle.html#build_lifecycle) still can take advantage of the [configuration cache](https://docs.gradle.org/current/userguide/configuration_cache.html) which significantly speeds up the total build process. The supposed usage is to have abstract implementation of the interface

**example to value source**
```kotlin
abstract class PassSecretValueSource : ValueSource<String, PassSecretValueSourceParameters> {

    override fun obtain(): String {
        // fetch data
    }
}
```

where the second generic type of the interface defines the input values that can be provided to the ValueSource, which in our case is the path to the secret inside the password store

**adding parameters to value sources**
```kotlin
public interface PassSecretValueSourceParameters : ValueSourceParameters {
    @get:Input
    val passName: Property<String>
}
```

The `PassSecretValueSource` can then be used via the [ProviderFactory](https://docs.gradle.org/current/javadoc/org/gradle/api/provider/ProviderFactory.html) in all Gradle files

**calling value sources**
```kotlin
providers.of(PassSecretValueSource::class) {
    this.parameters.passName = "some/pass/secret/name"
}.get()
```

## Overview

The full implementation needs to handle a few more details

* we use the injected Gradle helper `ExecOperations` to run the command line tooling to retrieve the secret (1)
* when the `CI` variable is set (2), we try to retrieve the secret from an environment variable, which is a slightly transformed version of the secret name to align with typical environment variable naming `some/pass/secret/name` -> `SOME_PASS_SECRET_NAME` (3)
* always provide explicit logging what is happening and where the data is pulled from. Especially in the error case try to be helpful and give all available information to make it easier to debug errors (4) 


**full solution**
```kotlin
public interface PassSecretValueSourceParameters : ValueSourceParameters {
    @get:Input
    val passName: Property<String>
}

val secretCache = mutableMapOf<String, String>()

abstract class PassSecretValueSource : ValueSource<String, PassSecretValueSourceParameters> {

    @get:Inject
    abstract val execOperations: ExecOperations // (1)

    override fun obtain(): String {
        val passName = parameters.passName.get()

        return secretCache.computeIfAbsent(passName) {
            if (System.getenv("CI") != null) { // (2)
                val envVarName = passName.replace("/", "_").uppercase( ) // (3)
                if (System.getenv(envVarName) != null) {
                    logger.info("found environment variable '${envVarName}' for pass secret '${passName}'")  // (4)
                    System.getenv(envVarName)
                } else {
                    throw RuntimeException("missing environment variable '${envVarName}' for pass secret '${passName}'") // (4)
                }
            } else {
                val stdout = ByteArrayOutputStream()
                val stderr = ByteArrayOutputStream()
                val result = execOperations.exec {
                    commandLine("pass", passName)
                    standardOutput = stdout
                    errorOutput = stderr
                }

                if (result.exitValue != 0) {
                    throw RuntimeException("could not retrieve pass secret '${passName}': ${stderr.toString(Charset.defaultCharset())}") // (4)
                }

                stdout.toString(Charset.defaultCharset())
            }
        }
    }
}
```

## Usage

Now that everything is wired together, the usage is quite straightforward. For example to set the environment variable `FOO` for all tests to the pass secret `some/pass/secret/name` (or `SOME_PASS_SECRET_NAME` if running in CI), configure the test task like this:

**example usage**
```kotlin
tasks.withType<Test> {

    environment(
        mapOf(
            "FOO" to providers.of(PassSecretValueSource::class) {
                this.parameters.passName = "some/pass/secret/name"
            }.get(),
        )
    )
}
```

Now when you use an IDE with Gradle support like IntelliJ or Eclipse, you can reload the Gradle config, run any test directly and be sure the environment variables have all needed secrets configured.  