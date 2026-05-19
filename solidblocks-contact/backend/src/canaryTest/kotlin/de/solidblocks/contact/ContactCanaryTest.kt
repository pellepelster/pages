package de.solidblocks.contact

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.WaitForSelectorState
import org.junit.jupiter.api.Test

class ContactCanaryTest {

  @Test
  fun `select terraform component and send contact request`() {
    Playwright.create().use { playwright ->
      val browser = playwright.chromium().launch()
      val context = browser.newContext()
      val page = context.newPage()

      page.navigate("https://solidblocks.de")

      val contact = page.locator("solidblocks-contact")
      contact.locator("app-cloud-component").first().waitFor()

      contact
        .locator("app-cloud-component")
        .filter(Locator.FilterOptions().setHasText("Terraform"))
        .click()

      contact.locator("input[type='email']").fill("playwright@solidblocks.de")
      contact.locator("button:has-text('Contact me')").click()

      contact
        .locator(".contact-success")
        .waitFor(Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE))
    }
  }
}
