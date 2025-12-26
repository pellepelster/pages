---
title: "Testing Terraform modules"
date: 2025-12-24T12:00:00+01:00
tags: [ "solidblocks", "test" ]
---

As the number of ready-to-use Terraform modules included in my open source [infrastructure components](https://pellepelster.github.io/solidblocks/#components) suite slowly grows, ensuring quality has been a constant topic during development. I wrote in [this](/posts/solidblocks-infra-test/) post about the challenges of testing infrastructure.

Dog-feeding the [infra-test](https://pellepelster.github.io/solidblocks/test/index.html) library I wrote back then to test my newly released [web-s3-docker](/posts/web-s3-docker/) module has lead to some major improvements around integration-testing Terraform modules that will be the main topic of this post.

The [infra-test](https://pellepelster.github.io/solidblocks/test/index.html) library is published to Maven central, and can be included like described [here](https://pellepelster.github.io/solidblocks/test/index.html#usage).

## Setup

Although it can be used without JUnit, it is recommended to make use of the Junit extension `SolidblocksTest` (➊) which ensures a clean resource cleanup after the tests are finished.


```kotlin
@ExtendWith(SolidblocksTest::class) // ➊
public class ReleaseTest {
    @Test
    fun testSnippets(context: SolidblocksTestContext) {
        // ...
    }
}
```

## Terraform

The Terraform wrapper (➊) created for the module in the folder `snippets/web-s3-docker-kitchen-sink` is configured by setting Terraform
(➋) as well as environment variables (➌).

The wrapper not only offers the typical terraform deployment cycle of `init` (➍) and `apply` (➎) but also allows to directly (and type safe) access the output produced by the Terraform module under test. In this case the module exposes the ip address of a created cloud VM (➌) as well as the private SSH key needed to access it.

When executed the wrapper makes sure the correct Terraform version for your operating and architecture is downloaded and executed.

```kotlin
@ExtendWith(SolidblocksTest::class)
public class ReleaseTest {

    @Test
    fun testSnippets(context: SolidblocksTestContext) {
        val terraform = context.terraform(Path.of("").resolve("snippets/web-s3-docker-kitchen-sink")) // ➊

        terraform.addVaribale("var1", "foo-bar") // ➋
        terraform.addEnvironmentVariable("HCLOUD_TOKEN", System.getenv("HCLOUD_TOKEN")) // ➌

        terraform.init() // ➍
        terraform.apply() // ➎

        val ipv4Address = output.getString("ipv4_address") // ➏
        val privateKey = output.getString("private_key") // ➐

    }
}
```

## Cloud-init

When the module is successfully applied, the cloud-init allows assertions to be made on the result of the cloud-init run that was used to provision the cloud VM. Using the address and credentials from the previous step, a cloud-init test context can be created (➊) that lets us wait for the provisioning to finish (➋) and if finished to make sure that it was successful (➌). In case those assertions fail, setting the flag `printOutputLogOnTestFailure` (➍) prints the cloud-init log to `stdout` to help with debugging the issue.

```kotlin
@ExtendWith(SolidblocksTest::class)
public class ReleaseTest {

    @Test
    fun testSnippets(context: SolidblocksTestContext) {

        // ...
        
        val ipv4Address = output.getString("ipv4_address") 
        val privateKey = output.getString("private_key")

        val cloudInit = context.cloudInit(ipv4Address, privateKey) // ➊

        cloudInit.printOutputLogOnTestFailure() // ➍

        await.atMost(1, TimeUnit.MINUTES).withPollInterval(ofSeconds(5)) until {
            cloudInit.isFinished() // ➋
        }

        cloudInit.result().shouldBeSuccess() // ➌

    }
}
```
For more details see the documentation [here](https://pellepelster.github.io/solidblocks/test/index.html).