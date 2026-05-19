package de.solidblocks.contact

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.mail.internet.InternetAddress
import java.io.File
import kotlinx.serialization.Serializable

@Serializable data class ContactRequest(val email: String, val components: List<String>)

private val logger = KotlinLogging.logger {}

fun Application.configureRouting(smtpConfig: SmtpConfig = smtpConfigFromEnv()) {
  val validComponentNames = loadValidComponentNames()
  val configYaml =
    Thread.currentThread()
      .contextClassLoader
      .getResourceAsStream("config.yml")
      ?.bufferedReader()
      ?.readText()

  routing {
    val staticDir = System.getenv("STATIC_DIR") ?: "static"
    staticFiles("/static", File(staticDir))

    route("/api") {
      get("/health") { call.respond(mapOf("status" to "ok")) }
      get("/home/config.yml") {
        if (configYaml == null) return@get call.respond(HttpStatusCode.NotFound)
        call.respondText(configYaml, ContentType.parse("text/yaml"))
      }
      rateLimit(RateLimitName("contact")) {
        post("/home/contact") {
          val request = call.receive<ContactRequest>()
          try {
            InternetAddress(request.email, true)
          } catch (_: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid email address"))
            return@post
          }
          if (request.components != listOf("anything")) {
            val invalid = request.components.filter { it !in validComponentNames }
            if (invalid.isNotEmpty()) {
              call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Invalid components: $invalid"),
              )
              return@post
            }
          }
          logger.info { "Contact: email=${request.email}, components=${request.components}" }
          smtpConfig.sendContactEmail(request.email, request.components)
          call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        }
      }
    }
  }
}
