package de.solidblocks.contact

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlinx.serialization.Serializable

@Serializable data class ContactRequest(val email: String, val components: List<String>)

fun Application.configureRouting() {
  val validComponentNames = loadValidComponentNames()
  val smtpConfig = smtpConfigFromEnv()

  routing {
    val staticDir = System.getenv("STATIC_DIR") ?: "static"
    staticFiles("/static", File(staticDir))

    route("/api") {
      get("/health") { call.respond(mapOf("status" to "ok")) }
      get("/home/config.yml") {
        val yaml =
          Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("config.yml")
            ?.bufferedReader()
            ?.readText() ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respondText(yaml, ContentType.parse("text/yaml"))
      }
      rateLimit(RateLimitName("contact")) {
        post("/home/contact") {
          val request = call.receive<ContactRequest>()
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
          call.application.log.info(
            "Contact: email=${request.email}, components=${request.components}"
          )
          smtpConfig.sendContactEmail(request.email, request.components)
          call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        }
      }
    }
  }
}
