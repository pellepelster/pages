---
title: "Solidblocks"
subtitle: Bespoke Application Hosting
date: 2024-02-20T22:00:00+01:00
draft: false
---

<div class="container">

{{< index-welcome image="/img/portrait.jpg" text="**I am an infrastructure specialist and software developer**. <br />Solidblocks is a managed hosting solution based on many years of infrastructure and application operation experience. With a focus on [Hetzner Cloud](https://cloud.hetzner.com) and [Scaleway](https://www.scaleway.com/en/) as deployment targets, it leans towards simple, easy-to-maintain architectures based on battle-tested open-source components. Solidblocks runs your applications safely and securely, without the ceremony, and with a human when you need one.<br /><br /> As an infrastructure specialist, I am happy to provide you with hands-on help and support for your application deployment needs." >}}

  <div class="container d-flex justify-content-center py-4 gap-5">
    <a href="https://console.blcks.de/?lang=en" class="btn btn-lg col-3 my-3 btn-danger align-self-center">
        <i class="fas fa-cloud"></i>
        blcks::console
    </a>
    <a href="contact/" class="btn btn-lg col-3 my-3 btn-primary align-self-center">
        <i class="fas fa-mail-bulk"></i>
        Contact
    </a>
  </div>

</div>

<div class="feature-divider"></div>

{{<feature-container title="Hosting">}}

    {{<feature-row>}}

        {{<feature title="Open Source" iconClasses="fa-brands fa-linux solidblocks-green" screenshot="postgresql-configuration.png">}}
          Host open-source solutions like Keycloak, PostgreSQL, GitLab, Grafana, HashiCorp Vault, and many more. Based on common and proven deployment patterns, custom open-source hosting on top of Solidblocks combines a stable hosting solution with the option to tailor the solution to your specific needs.
        {{</feature>}}

        {{<feature title="Custom Applications" iconClasses="fa-industry solidblocks-orange" screenshot="service-create.png">}}
          Solidblocks hosts everything from greenfield JVM or .NET projects and Python- or Ruby-based solutions to mature PHP applications. Its extensibility makes Solidblocks the perfect platform to host your bespoke business applications, or give legacy applications a new life and a stable environment.
        {{</feature>}}

        {{<feature title="Tailored Managed Services" iconClasses="fa-sliders solidblocks-yellow" screenshot="java-spring-configuration.png">}}
          The managed hosting for your custom applications is tailored to the needs of your workload and includes automatic instrumentation for metrics and logs.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Your Servers & Data" iconClasses="fa-cloud solidblocks-yellow" screenshot="postgresql-exports.png">}}
          For managed solutions, all resources can be hosted in your own cloud accounts, ensuring you always have access to all servers and data. This applies to all services used to build your solution, such as AWS, Hetzner Cloud, and more. Since you own the accounts, you can take control at any time.
        {{</feature>}}

        {{<feature title="Virtual Machines & Servers" iconClasses="fa-server solidblocks-green" screenshot="servers-list.png">}}
          Solidblocks is built around virtual or physical servers. This lets you pick from a wide range of cloud providers, or even deploy to on-premises hardware to make better use of existing gear.
        {{</feature>}}

        {{<feature title="Developer Experience" iconClasses="fa-play solidblocks-orange" screenshot="postgresql-connect.png">}}
          The deployment process is designed for ease of use on developer machines and for integration into common CI/CD systems like GitHub, GitLab, Jenkins, and others. The <code>blcks-cli</code> connects you to your services from your own machine.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Secret Rotation" iconClasses="fa-key solidblocks-orange" screenshot="postgresql-rotate-secrets.png">}}
          All secrets and user credentials can be rotated at any time to mitigate the risk of long-lived credentials that may leak over time. This also allows for a quick revocation of existing secrets and credentials in case of a leak.
        {{</feature>}}

        {{<feature title="Backups" iconClasses="fa-cloud-arrow-up solidblocks-yellow" screenshot="postgresql-backups.png">}}
          Encrypted data backups to other clouds like AWS or GCP provide an extra layer of security for your data and reduce the blast radius in case of accidental data deletion or configuration mistakes.
        {{</feature>}}

        {{<feature title="CVE Scans" iconClasses="fa-magnifying-glass solidblocks-green" screenshot="server-sbom.png">}}
          Automated CVE scans, coupled with an always up-to-date SBOM, make it easy to discover security vulnerabilities early on and to mitigate them.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Environments" iconClasses="fa-layer-group solidblocks-yellow" screenshot="environments-list.png">}}
          Multi-environment support is a first-class citizen. It can be used to support your application lifecycle and to provide different test environments.
        {{</feature>}}

        {{<feature title="Logs" iconClasses="fa-file-lines solidblocks-green" screenshot="postgresql-logs.png">}}
          Logging is included for all managed services, which makes it easier to monitor and debug your workloads.
        {{</feature>}}

        {{<feature title="Metrics" iconClasses="fa-chart-line solidblocks-orange" screenshot="postgresql-metrics.png">}}
          Application and server metrics are always gathered, helping you detect and visualize application usage, performance and long-term trends, and supporting sizing decisions.
        {{</feature>}}

    {{</feature-row>}}

    {{<feature-row>}}

        {{<feature title="Deployment" iconClasses="fa-infinity solidblocks-green" screenshot="cli-download.png">}}
          A powerful CLI makes it easy to integrate deployments into all major CI/CD systems or into already existing application build pipelines.
        {{</feature>}}

    {{</feature-row>}}

{{</feature-container>}}

