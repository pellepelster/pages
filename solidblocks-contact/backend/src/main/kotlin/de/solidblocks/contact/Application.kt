package de.solidblocks.contact

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.time.Duration.Companion.hours

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module(smtpConfig: SmtpConfig = smtpConfigFromEnv()) {
  install(CORS) {
    allowHost("solidblocks.de", schemes = listOf("https"))
    allowHost("pelle.io", schemes = listOf("https"))
    allowHost("localhost:1313")
    allowHost("localhost:4200")
    allowHeader(HttpHeaders.ContentType)
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Post)
  }
  install(ContentNegotiation) { json() }
  install(XForwardedHeaders)
  install(RateLimit) {
    register(RateLimitName("contact")) {
      rateLimiter(limit = 5, refillPeriod = 1.hours)
      requestKey { call ->
        call.request.headers["X-Forwarded-For"]?.split(",")?.firstOrNull()?.trim()
          ?: call.request.local.remoteHost
      }
    }
  }
  configureRouting(smtpConfig)
}
