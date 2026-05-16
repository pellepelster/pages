package de.solidblocks.contact

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module() {
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
  configureRouting()
}
