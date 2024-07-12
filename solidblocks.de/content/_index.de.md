---
title: "Solidblocks"
subtitle: Bespoke Application Hosting
date: 2024-02-20T22:00:00+01:00
draft: false
---

{{<livehelperchat_widget lang="ger">}}

<div class="container">

Solidblocks ist eine Sammlung von Komponenten und bewährten Verfahren rund um die Bereitstellung und den Betrieb von Cloud-Infrastrukturen und Anwendungen. Mit Fokus auf der [Hetzner Cloud](https://cloud.hetzner.com) als Infrastrukur-Provider, aufbauend auf bewährten Open-Source-Komponenten stehen einfache gut zu wartende Architekturen im Vordergrund.

Viele Komponenten sind bereits als [Open-Source](https://github.com/pellepelster/solidblocks) verfügbar und als Infrastruktur-Spezialist stehe ich mit Rat und Tat bei der Umsetzung Ihrer Cloud-Infrastrukturen zur Verfügung. Von der schlüsselfertigen Komplett-Lösungen bis hin zu teilweise oder vollständig verwalteten Umgebungen für Ihre Anwendungen.

  <div class="container d-flex justify-content-center py-4">
      <a href="contact" class="btn btn-lg col-4 my-3 btn-primary align-self-center">
          <i class="fas fa-mail-bulk"></i>
          Kontakt
      </a>
  </div>

</div>

<div class="feature-divider"></div>

{{<feature-container title="Hosting">}}

  {{<feature-row>}}

    {{<feature title="Open Source" iconClasses="fa-brands fa-linux solidblocks-green">}}
        Fertige Lösungen für den Betrieb von Open-Source-Lösungen wie z.B.Keycloak, PostgreSQL, GitLab, Grafana, HashiCorp Vault und viele mehr. Open-Source-Hosting auf Solidblocks verbindet bewährten  und stabile Bausteine mit der Möglichkeit, die Lösung an Ihre speziellen Anforderungen anzupassen.
{{</feature>}}
    
    {{<feature title="Kundenspezifische Anwendungen" iconClasses="fa-industry solidblocks-orange">}}
        Von greenfield JVM oder .NET Core Projekten, über Python und Ruby-basierte Lösungen, bis hin zu brownfield PHP-Anwendungen. Die Erweiterbarkeit von Solidblocks macht es zur perfekten Plattform, um Ihre maßgeschneiderten Geschäftsanwendungen zu hosten, oder um älteren Anwendungen ein neues Leben und eine stabile Umgebung zu geben.
    {{</feature>}}

    {{<feature title="Wartung & Instandsetzung" iconClasses="fa-screwdriver-wrench solidblocks-yellow">}}
        Abhängig von der Implementierung und zugrunde liegenden Framework Ihrer Anwendung können Solidblocks-Komponenten, wie Keycloak oder OpenVPN helfen, die Sicherheit Ihrer Anwendung zu verbessern und die Lebensdauer verlängern.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>
{{<feature-container title="Unkompliziert & Pragmatisch">}}

{{<feature-row>}}
    {{<feature title="Ihr Code" iconClasses="fa-code-branch solidblocks-orange">}}
        Sie haben jederzeit vollen Zugriff auf den Sourcecode Ihrer Lösung; es gibt keine proprietären Komponenten, Sie können den Code jederzeit forken, falls nötig. Je nach Umfang der Lösung können Pairing und/oder das gemeinsame Arbeiten am Code gewährleisten, dass das Wissen und die Abläufe, die zur Wartung und zum Betrieb Ihrer Anwendungen erforderlich sind, gut verteilt sind.
    {{</feature>}}
    
    {{<feature title="Ihre Server, Ihre Daten" iconClasses="fa-cloud solidblocks-yellow">}}
        Ale Ressourcen für Ihre Lösungen können in Ihren eigenen Cloud-Accounts gehostet werden, um sicherzustellen, dass Sie jederzeit Zugang zu allen VMs und Daten haben. Dies gilt für alle Services, die für Ihre Lösung nötig sind, wie z. B. AWS, Hetzner Cloud, Elastic.co und mehr, so dass Sie jederzeit die Kontrolle übernehmen können.
    {{</feature>}}

    {{<feature title="Virtuelle Maschinen und Server" iconClasses="fa-server solidblocks-green">}}
        Obwohl eine ausgereifte Container-Orchestrierung wie z.B. Kubernetes ihre Vorteile hat, kann manchmal eine einfachere Lösung auf Basis von Virtuellen Maschinen (VMs) oder Bare-Metal Servern kosteneffektiver, einfacher zu handhaben, zu warten und zu betreiben sein. Falls möglich, ist auch der Einsatz auf vorhandener Hardware eine Option, um bereits vorhandene Maschinen besser zu nutzen.
    {{</feature>}}


  {{</feature-row>}}

  {{<feature-row>}}

    {{<feature title="Infrastructure as Code" iconClasses="fa-code solidblocks-green">}}
        Alle deployten Ressourcen werden mit Infrastructure-as-Code-Lösungen wie Terraform, OpenTofu und Ansible entwickelt und deployt. Für Anwendungen, die keine Automatisierung bereitstellen, können spezielle Lösungen implementiert und in den deployment lifecycle integriert werden.
    {{</feature>}}
    
    {{<feature title="Developer Experience" iconClasses="fa-play solidblocks-orange">}}
        Der deployment cycle ist so konzipiert, dass er auf Entwicklermaschinen einfach zu verwenden ist und in gängige CI/CD-Systeme wie GitHub, GitLab, Jenkins und andere integriert werden kann. Jede Lösung wird mit einem entwicklerfreundlichen build system geliefert, das deployment und Interaktion mit den bereitgestellten Ressourcen erleichtert.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="Sicherheit & Daten">}}
  {{<feature-row>}}
    
    {{<feature title="Updates" iconClasses="fa-arrows-rotate solidblocks-orange">}}
        Alle Komponenten wie Betriebssysteme und Softwarepakete werden regelmäßig aktualisiert. Wenn möglich, sind Tools wie <a href="https://github.com/renovatebot/renovate">renovate</a> in den deployment Prozess integriert, um sicherzustellen, dass alles immer auf dem neuesten Stand ist.
    {{</feature>}}
    
    {{< feature title="Verschlüsselung" iconClasses="fa-lock solidblocks-green">}}
        Alle außerhalb der Cloud gespeicherten Daten, wie z.B. Backups, sind standardmäßig verschlüsselt, um sicherzustellen das sensible Daten nicht in die falschen Hände gelangen können.
    {{</feature>}}
    
    {{< feature title="Passworte" iconClasses="fa-key solidblocks-yellow">}}
        Alle Passworte und sonstige sensible Daten können jederzeit neu generiert und rotiert werden, um das Risiko von langlebigen Passworten, die im Laufe der Zeit verloren gehen könnten, zu mindern. Dies ermöglicht auch bestehende Passworte im Falle eines Lecks schnell zu deaktivieren.
    {{</feature>}}
    
  {{</feature-row>}}

  {{<feature-row>}}

    {{< feature title="Backups" iconClasses="fa-cloud-arrow-up solidblocks-yellow">}}
        Verschlüsselte Datenbackups auf Clouds wie AWS oder GCP bieten eine zusätzliche Sicherheitsebene für Ihre Daten und reduzieren den Schaden im Falle von versehentlichem Löschen von Daten oder Konfigurationsfehlern.
    {{</feature>}}

    {{< feature title="CVE Scanning" iconClasses="fa-magnifying-glass solidblocks-green">}}
      Automatisierte CVE-Scans, zusammen mit einem stets aktuellen SBOM, erleichtern es sicherheitskritische Bugs frühzeitig zu entdecken und zu beheben.
    {{</feature>}}

    {{< feature title="IDP/IAM" iconClasses="fa-id-card solidblocks-orange">}}
        IDM-Lösungen wie <a href="https://www.keycloak.org">Keycloak</a> können leicht integriert werden, um Ihre Anwendung abzusichern oder in Verbindung mit <a href="https://www.vaultproject.io">Hashicorp Vault</a>, um den SSH-Zugriff auf Ihre VMs mit kurzlebigen credentials abzusichern.
    {{</feature>}}
    
  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="Deployment Lifecycle" >}}
  {{<feature-row>}}

    {{<feature title="Umgebungen" iconClasses="fa-layer-group solidblocks-yellow">}}
        Alle Lösungen untersützen von Haus aus das mehrere Umgebungen. Diese können verwendet werden, um Ihren Anwendungslebenszyklus abzubilden, oder um verschiedene Testumgebungen bereitzustellen.
    {{< /feature>}}
    
    {{<feature title="Bootstrapping" iconClasses="fa-terminal solidblocks-orange">}}
        Das Löschen und Neu-Aufbaue von Umgebungen wird systematisch getestet, um sicherzustellen, dass es keine versteckten zyklischen Abhängigkeiten in der Infrastruktur gibt. Die stellt auch sicher, dass der Code der nur während des Bootstrapping ausgeführt wird, weiterhin korrekt funktioniert.
    {{</feature>}}
    
    {{<feature title="Disaster Recovery" iconClasses="fa-fire solidblocks-green">}}
        Die Wiederherstellung von Umgebungen aus Backups wird regelmäßig getestet und ist sowohl in den Playbooks als auch in den Entwicklerbriefings enthalten. Alle Komponenten sind so desingt, dass die gesamte Umgebung jederzeit gelöscht und dann vollständig aus den Backups wieder aufgebaut werden kann.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container >}}

<div class="feature-divider"></div>

{{<feature-container title="Logging & Monitoring" >}}
  {{<feature-row>}}

    {{<feature title="Logs" iconClasses="fa-server solidblocks-green">}}
        Logging-Plattformen wie Elastic.co können benutzt werden, um alle Anwendungs- und VM-Logs zentral zu speichern, und helfen Fehler frühzeitig zu erkennen und zu beheben. Darüber hinaus erleichtern sie das debuggen von Fehlern.
    {{</feature>}}

    {{<feature title="Metriken" iconClasses="fa-chart-line solidblocks-orange">}}
        Anwendungs- und VM-Metriken können auf Analyseplattformen, wie Grafana oder Elastic.co, gesammelt werden. Zentrale Metriken helfen bei der Erkennung und Visualisierung von Anwendungsnutzung, Leistung, langfristigen Trends und können darüber hinaus Hinweise für Sizing-Entscheidungen geben.
    {{</feature>}}

    {{<feature title="Tagging" iconClasses="fa-tag solidblocks-yellow">}}
        Alle Logs  und Metriken werden mit Informationen über die Umgebung, den Service, die Version usw. getagged und mit Ereignissen wie z.B. deployments angereichert. Dies erleichtert die Korrelation von Problemen und Bugs mit verschiedenen Versionen und Bereitstellungen.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="CI/CD" >}}

  {{<feature-row>}}

    {{<feature title="Deployment" iconClasses="fa-infinity solidblocks-yellow">}}
        Die deployment Prozess kann leicht in alle CI/CD-Systeme oder in bereits bestehende Pipelines integriert werden.
    {{</feature>}}

    {{< feature title="Tests" iconClasses="fa-crow solidblocks-green">}}
        Integrationstests im deployment Prozess stellen sicher, dass das deployment erfolgreich ist. Sie dienen auch als Canary, um zu alarmieren wenn Teile der Infrastruktur oder Anwendung defekt sind, oder die Performance eingeschränkt ist.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="Dokumentation & Support" >}}

  {{<feature-row>}}

    {{<feature title="Playbooks" iconClasses="fa-list-ol solidblocks-green">}}
        Playbooks sind ein fester Bestandteil jeder Lösung, und beschreiben detaillierte Verfahren für Szenarien wie die Wiederherstellung von Umgebungen oder die Rotation von credentials in Notfällen. Sie enhalten auch Informationen für den allgemeinen Betrieb und die Wartung.
    {{</feature>}}

    {{<feature title="Übungen" iconClasses="fa-fire-extinguisher solidblocks-orange">}}
        Regelmäßige Übungen stellen sicher, dass wichtige Schritte, die selten genutzt werden nach wie vor funktionieren, und dass sich die Entwickler mit ihrer Durchführung sicher fühlen.
    {{</feature>}}

    {{<feature title="Support" iconClasses="fa-phone solidblocks-yellow">}}
        Im Falle kritischer Fehler, wie Systemabstürzen oder Datenverlust, ist deutsch- und englischsprachiger Notfallsupport per E-Mail, Telefon oder Chat verfügbar.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}
