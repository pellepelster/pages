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

val ktorVersion = "3.0.3"

dependencies {
  implementation("io.ktor:ktor-server-core:$ktorVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
  implementation("io.ktor:ktor-server-cors:$ktorVersion")
  implementation("org.eclipse.angus:angus-mail:2.0.3")
  implementation("org.yaml:snakeyaml:2.2")
  implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
  testImplementation(kotlin("test"))
}
