---
title: "Solidblocks"
subtitle: Bespoke Application Hosting
date: 2024-02-20T22:00:00+01:00
draft: false
---

<div class="container">

{{< index-welcome image="/img/portrait.jpg" text="**Ich bin Infrastruktur-Spezialist und Softwareentwickler**. <br />Solidblocks ist eine Managed-Hosting-Lösung, die auf vielen Jahren Erfahrung im Betrieb von Infrastruktur und Anwendungen aufbaut. Mit Fokus auf [Hetzner Cloud](https://cloud.hetzner.com) und [Scaleway](https://www.scaleway.com/en/) als Deployment-Ziele setzt Solidblocks auf einfache, gut wartbare Architekturen aus bewährten Open-Source-Komponenten. Solidblocks betreibt Ihre Anwendungen sicher und zuverlässig, ohne Umwege, und mit einem Menschen, wenn Sie einen brauchen.<br /><br /> Als Infrastruktur-Spezialist unterstütze ich Sie gerne mit Rat und Tat bei Ihren Anwendungen." >}}

  <div class="container d-flex justify-content-center py-4 gap-5">
    <a href="https://console.blcks.de/?lang=de" class="btn btn-lg col-3 my-3 btn-danger align-self-center">
        <i class="fas fa-cloud"></i>
        blcks::console
    </a>
    <a href="contact/" class="btn btn-lg col-3 my-3 btn-primary align-self-center">
        <i class="fas fa-mail-bulk"></i>
        Kontakt
    </a>
  </div>

</div>

<div class="feature-divider"></div>

{{<feature-container title="Hosting">}}

    {{<feature-row>}}

        {{<feature title="Open Source" iconClasses="fa-brands fa-linux solidblocks-green" screenshot="postgresql-configuration.png">}}
          Betreiben Sie Open-Source-Lösungen wie Keycloak, PostgreSQL, GitLab, Grafana, HashiCorp Vault und viele mehr. Auf Basis gängiger und bewährter Deployment-Muster verbindet Solidblocks individuelles Open-Source-Hosting mit der Möglichkeit, die Lösung an Ihre Anforderungen anzupassen.
        {{</feature>}}

        {{<feature title="Individuelle Anwendungen" iconClasses="fa-industry solidblocks-orange" screenshot="service-create.png">}}
          Solidblocks betreibt alles von JVM- oder .NET-Projekten über Python- oder Ruby-basierte Lösungen bis hin zu gewachsenen PHP-Anwendungen. Durch seine Erweiterbarkeit ist Solidblocks die perfekte Plattform für Ihre individuellen Geschäftsanwendungen und gibt Legacy-Anwendungen ein neues Leben in einer stabilen Umgebung.
        {{</feature>}}

        {{<feature title="Maßgeschneiderte Managed Services" iconClasses="fa-sliders solidblocks-yellow" screenshot="java-spring-configuration.png">}}
          Das Managed Hosting für Ihre individuellen Anwendungen ist auf die Anforderungen Ihres Workloads zugeschnitten, inklusive automatischer Instrumentierung für Metriken und Logs.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Ihre Server & Daten" iconClasses="fa-cloud solidblocks-yellow" screenshot="postgresql-exports.png">}}
          Bei verwalteten Lösungen können alle Ressourcen in Ihren eigenen Cloud-Konten betrieben werden, sodass Sie jederzeit Zugriff auf alle Server und Daten haben. Das gilt für alle Dienste, aus denen Ihre Lösung besteht, zum Beispiel AWS, Hetzner Cloud und weitere. Da Ihnen die Konten gehören, können Sie jederzeit die Kontrolle übernehmen.
        {{</feature>}}

        {{<feature title="Virtuelle Maschinen & Server" iconClasses="fa-server solidblocks-green" screenshot="servers-list.png">}}
          Solidblocks basiert auf virtuellen oder physischen Servern. So können Sie aus einer großen Auswahl an Cloud-Anbietern wählen oder sogar auf eigene Hardware vor Ort deployen, um vorhandene Hardware besser zu nutzen.
        {{</feature>}}

        {{<feature title="Developer Experience" iconClasses="fa-play solidblocks-orange" screenshot="postgresql-connect.png">}}
          Der Deployment-Prozess ist für die einfache Nutzung auf Entwicklerrechnern und für die Integration in gängige CI/CD-Systeme wie GitHub, GitLab, Jenkins und andere ausgelegt. Die <code>blcks-cli</code> verbindet Sie von Ihrem eigenen Rechner aus mit Ihren Services.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Secret-Rotation" iconClasses="fa-key solidblocks-orange" screenshot="postgresql-rotate-secrets.png">}}
          Alle Secrets und Zugangsdaten können jederzeit rotiert werden, um das Risiko langlebiger Zugangsdaten zu verringern, die mit der Zeit nach außen gelangen können. Bei einem Leak lassen sich bestehende Secrets und Zugangsdaten so auch schnell widerrufen.
        {{</feature>}}

        {{<feature title="Backups" iconClasses="fa-cloud-arrow-up solidblocks-yellow" screenshot="postgresql-backups.png">}}
          Verschlüsselte Backups in andere Clouds wie AWS oder GCP bieten eine zusätzliche Sicherheitsebene für Ihre Daten und begrenzen den Schaden bei versehentlich gelöschten Daten oder Konfigurationsfehlern.
        {{</feature>}}

        {{<feature title="CVE-Scans" iconClasses="fa-magnifying-glass solidblocks-green" screenshot="server-sbom.png">}}
          Automatisierte CVE-Scans in Verbindung mit einer stets aktuellen SBOM machen es einfach, Sicherheitslücken früh zu erkennen und zu beheben.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Umgebungen" iconClasses="fa-layer-group solidblocks-yellow" screenshot="environments-list.png">}}
          Mehrere Umgebungen werden von Grund auf unterstützt. Sie begleiten den Lebenszyklus Ihrer Anwendung und ermöglichen es unterschiedliche Versionen und Konfigurationen in verschiedenen Umgebungen bereit zu stellen.
        {{</feature>}}

        {{<feature title="Logs" iconClasses="fa-file-lines solidblocks-green" screenshot="postgresql-logs.png">}}
          Logging ist in allen Managed Services enthalten. Das macht es einfacher, Ihre Workloads zu überwachen und Fehler zu finden.
        {{</feature>}}

        {{<feature title="Metriken" iconClasses="fa-chart-line solidblocks-orange" screenshot="postgresql-metrics.png">}}
          Anwendungs- und Server-Metriken werden immer erfasst. Sie helfen Ihnen, Nutzung, Performance und langfristige Trends Ihrer Anwendung zu erkennen und zu visualisieren, und unterstützen Entscheidungen zur Dimensionierung.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Deployment" iconClasses="fa-infinity solidblocks-green" screenshot="cli-download.png">}}
          Eine leistungsfähige CLI macht es einfach, Deployments in alle gängigen CI/CD-Systeme oder in bestehende Build-Pipelines zu integrieren.
        {{</feature>}}

    {{</feature-row>}}

{{</feature-container>}}
