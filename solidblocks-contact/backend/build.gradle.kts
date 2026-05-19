plugins {
  kotlin("jvm") version "2.3.10"
  kotlin("plugin.serialization") version "2.3.10"
  application
  id("com.diffplug.spotless") version "7.0.4"
}

group = "de.solidblocks.contact"

version = "0.0.1"

application { mainClass.set("de.solidblocks.contact.ApplicationKt") }

repositories { mavenCentral() }

spotless {
  kotlin {
    ktfmt().googleStyle()
    trimTrailingWhitespace()
    endWithNewline()
  }
  kotlinGradle {
    ktfmt().googleStyle()
    trimTrailingWhitespace()
    endWithNewline()
  }
}

val canaryTest by sourceSets.creating

configurations["canaryTestImplementation"].extendsFrom(configurations.testImplementation.get())

configurations["canaryTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

val ktorVersion = "3.0.3"
val playwrightVersion = "1.50.0"
val testcontainersVersion = "1.21.3"

dependencies {
  implementation("io.ktor:ktor-server-core:$ktorVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
  implementation("io.ktor:ktor-server-cors:$ktorVersion")
  implementation("io.ktor:ktor-server-rate-limit:$ktorVersion")
  implementation("io.ktor:ktor-server-forwarded-header:$ktorVersion")
  implementation("org.eclipse.angus:angus-mail:2.0.3")
  implementation("org.yaml:snakeyaml:2.2")
  implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
  testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
  testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
  testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  "canaryTestImplementation"("org.junit.jupiter:junit-jupiter:5.11.0")
  "canaryTestImplementation"("com.microsoft.playwright:playwright:$playwrightVersion")
  "canaryTestRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") { useJUnitPlatform() }

tasks.register<Test>("canaryTest") {
  description = "Runs canary tests against live environments"
  group = "verification"
  testClassesDirs = sourceSets["canaryTest"].output.classesDirs
  classpath = sourceSets["canaryTest"].runtimeClasspath
  useJUnitPlatform()
}

tasks.register<JavaExec>("installPlaywrightBrowsers") {
  description = "Installs Playwright browser binaries required for canary tests"
  group = "verification"
  classpath = sourceSets["canaryTest"].runtimeClasspath
  mainClass.set("com.microsoft.playwright.CLI")
  args = listOf("install", "--with-deps", "chromium")
}
