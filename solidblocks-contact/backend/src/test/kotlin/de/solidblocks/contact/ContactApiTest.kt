package de.solidblocks.contact

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Serializable
private data class MailpitMessageSummary(
  @SerialName("ID") val id: String,
  @SerialName("Subject") val subject: String,
)

@Serializable
private data class MailpitMessagesResponse(
  val messages: List<MailpitMessageSummary>? = null,
  val total: Int,
)

@Serializable
private data class MailpitMessageDetail(
  @SerialName("ID") val id: String,
  @SerialName("Subject") val subject: String,
  @SerialName("Text") val text: String,
)

@Testcontainers
class ContactApiTest {

  companion object {
    @Container
    @JvmStatic
    val mailpit: GenericContainer<*> =
      GenericContainer("axllent/mailpit:latest")
        .withExposedPorts(1025, 8025)
        .waitingFor(Wait.forHttp("/api/v1/info").forPort(8025))
  }

  private val http = HttpClient.newHttpClient()
  private val json = Json { ignoreUnknownKeys = true }

  private fun smtpConfig() =
    SmtpConfig(
      host = mailpit.host,
      port = mailpit.getMappedPort(1025),
      username = "test",
      password = "test",
      contactRecipient = "ops@test.local",
      contactFrom = "contact@test.local",
      ssl = false,
    )

  private fun mailpitGet(path: String): String =
    http
      .send(
        HttpRequest.newBuilder()
          .uri(URI("http://${mailpit.host}:${mailpit.getMappedPort(8025)}$path"))
          .GET()
          .build(),
        BodyHandlers.ofString(),
      )
      .body()

  private fun messages(): List<MailpitMessageSummary> =
    json.decodeFromString<MailpitMessagesResponse>(mailpitGet("/api/v1/messages")).messages
      ?: emptyList()

  private fun messageDetail(id: String): MailpitMessageDetail =
    json.decodeFromString(mailpitGet("/api/v1/message/$id"))

  @BeforeEach
  fun clearMailbox() {
    http.send(
      HttpRequest.newBuilder()
        .uri(URI("http://${mailpit.host}:${mailpit.getMappedPort(8025)}/api/v1/messages"))
        .DELETE()
        .build(),
      BodyHandlers.discarding(),
    )
  }

  @Test
  fun `valid contact request returns 200 and delivers email with correct subject and body`() =
    testApplication {
      application { module(smtpConfig()) }

      val response =
        client.post("/api/home/contact") {
          contentType(ContentType.Application.Json)
          setBody("""{"email":"user@example.com","components":["Playbooks"]}""")
        }

      assertEquals(HttpStatusCode.OK, response.status)

      val msgs = messages()
      assertEquals(1, msgs.size)

      val detail = messageDetail(msgs.first().id)
      assertEquals("New contact request from user@example.com", detail.subject)
      assertTrue(detail.text.contains("Email: user@example.com"))
      assertTrue(detail.text.contains("Components: Playbooks"))
    }

  @Test
  fun `invalid email format returns 400 and sends no email`() = testApplication {
    application { module(smtpConfig()) }

    val response =
      client.post("/api/home/contact") {
        contentType(ContentType.Application.Json)
        setBody("""{"email":"not-an-email","components":["Playbooks"]}""")
      }

    assertEquals(HttpStatusCode.BadRequest, response.status)
    assertTrue(response.bodyAsText().contains("Invalid email address"))
    assertEquals(0, messages().size)
  }

  @Test
  fun `email with CRLF injection attempt returns 400`() = testApplication {
    application { module(smtpConfig()) }

    val response =
      client.post("/api/home/contact") {
        contentType(ContentType.Application.Json)
        setBody(
          """{"email":"attacker@evil.com\r\nBcc:victim@x.com","components":["Playbooks"]}"""
        )
      }

    assertEquals(HttpStatusCode.BadRequest, response.status)
    assertEquals(0, messages().size)
  }

  @Test
  fun `unknown component returns 400 and sends no email`() = testApplication {
    application { module(smtpConfig()) }

    val response =
      client.post("/api/home/contact") {
        contentType(ContentType.Application.Json)
        setBody("""{"email":"user@example.com","components":["NonExistentComponent"]}""")
      }

    assertEquals(HttpStatusCode.BadRequest, response.status)
    assertEquals(0, messages().size)
  }

  @Test
  fun `wildcard components value bypasses component validation`() = testApplication {
    application { module(smtpConfig()) }

    val response =
      client.post("/api/home/contact") {
        contentType(ContentType.Application.Json)
        setBody("""{"email":"user@example.com","components":["anything"]}""")
      }

    assertEquals(HttpStatusCode.OK, response.status)
    assertEquals(1, messages().size)
  }
}
