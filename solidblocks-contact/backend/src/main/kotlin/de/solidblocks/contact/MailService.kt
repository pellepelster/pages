package de.solidblocks.contact

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Properties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val log = KotlinLogging.logger {}

data class SmtpConfig(
  val host: String,
  val port: Int,
  val username: String,
  val password: String,
  val contactRecipient: String,
  val contactFrom: String,
)

fun smtpConfigFromEnv(): SmtpConfig {
  val host = System.getenv("SMTP_HOST") ?: throw RuntimeException("SMTP_HOST not set")
  val username = System.getenv("SMTP_USERNAME") ?: throw RuntimeException("SMTP_USERNAME not set")
  val password = System.getenv("SMTP_PASSWORD") ?: throw RuntimeException("SMTP_PASSWORD not set")
  val recipient =
    System.getenv("SMTP_RECIPIENT") ?: throw RuntimeException("SMTP_RECIPIENT not set")
  val from = System.getenv("SMTP_FROM") ?: throw RuntimeException("SMTP_FROM not set")
  val port = System.getenv("SMTP_PORT")?.toIntOrNull() ?: 587

  return SmtpConfig(host, port, username, password, recipient, from)
}

suspend fun SmtpConfig.sendContactEmail(email: String, components: List<String>) =
  withContext(Dispatchers.IO) {
    val props =
      Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.ssl.enable", "true")
        put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3")
        put("mail.smtp.host", host)
        put("mail.smtp.port", port.toString())
      }
    val session =
      Session.getInstance(
        props,
        object : Authenticator() {
          override fun getPasswordAuthentication() = PasswordAuthentication(username, password)
        },
      )
    val message =
      MimeMessage(session).apply {
        setFrom(InternetAddress(contactFrom))
        setRecipient(Message.RecipientType.TO, InternetAddress(contactRecipient))
        subject = "New contact request from $email"
        setText(
          """
          Email: $email
          Components: ${components.joinToString(", ")}
          """
            .trimIndent()
        )
      }

    Transport.send(message)
    log.info { "Contact email sent to=$contactRecipient requester=$email" }
  }
