---
title: "Solidblocks"
subtitle: Bespoke Application Hosting
date: 2024-02-20T22:00:00+01:00
draft: false
carousel:
  - src: "/features/do_file1.png"
    alt: "Developer Tooling"
    caption: "Extensive developer tooling for provisioning, testing and maintenance"
    url: "developer-experience"
  - src: "/features/runbook1.png"
    alt: "Runbooks"
    caption: "Detailed runbooks for day-to-day operations"
  - src: "/features/rds_cluster1.png"
    alt: "Blueprint RDS"
    caption: "Battle proven PostgreSQL solutions with integrated backups"
  - src: "/features/k3s_blueprint.png"
    alt: "Blueprint K3S"
    caption: "Tested blueprints for common infrastructure setups"
---

<div class="container">

Solidblocks is a collection of components, patterns and best practices to deliver cloud infrastructures and application deployments. With a focus on the [Hetzner Cloud](https://cloud.hetzner.com) as deployment target it leans towards simple and easy to maintain architectures based on battle-tested open source components.

 Many components are available as [open source](https://pellepelster.github.io/solidblocks/) components. As an infrastructure specialist, I am happy to provide you with hands on help and support for your application deployment needs, ranging from turnkey ready-to-use solutions to partially or fully managed environments for your applications.
  
  <div class="container d-flex justify-content-center py-4">
      <a href="contact/" class="btn btn-lg col-4 my-3 btn-primary align-self-center">
          <i class="fas fa-mail-bulk"></i>
          Contact
      </a>
  </div>

</div>

<div class="feature-divider"></div>

<div class="container py-5">
    <h2 class="pb-2 border-bottom">Features</h2>
    {{< feature-carousel >}}
</div>

<div class="feature-divider"></div>

{{<feature-container title="Hosting">}}

  {{<feature-row>}}

    {{<feature title="Open Source" iconClasses="fa-brands fa-linux solidblocks-green">}}
      Host open source solutions like Keycloak, PostgreSQL, GitLab, Grafana, HashiCorp Vault, and many more. Based on common and proven deployment patterns, custom open-source hosting on top of Solidblocks combines a stable hosting solution with the option to taylor the solution to your specific needs.    
    {{</feature>}}
    
    {{<feature title="Custom Applications" iconClasses="fa-industry solidblocks-orange">}}
      From greenfield JVM or .NET Core projects, over Python and Ruby-based solutions, to matured PHP applications. Solidblocks extendability makes it the perfect platform to host your bespoke business applications, or give legacy applications a new life and a stable environment.
    {{</feature>}}

    {{<feature title="Maintenance & Overhaul" iconClasses="fa-screwdriver-wrench solidblocks-yellow">}}
        Depending on the underlying ecosystem or framework of your application, Solidblocks components, such as Keycloak or Woreguard, can be retrofitted to strengthen the security of your application. This will secure your application, thereby extending its lifetime.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>
{{<feature-container title="Simplicity">}}

{{<feature-row>}}
    {{<feature title="Your Code" iconClasses="fa-code-branch solidblocks-orange">}}
      You have complete ownership of the code for your solution; there are no proprietary components, you can fork it anytime you want if necessary. Depending on the scope and goal of the solution, pairing and/or co-creating on your code can ensure that the knowledge and the routines necessary to maintain and operate your applications are well distributed.
    {{</feature>}}
    
    {{<feature title="Your Servers & Data" iconClasses="fa-cloud solidblocks-yellow">}}
      For managed solutions, all resources can be hosted in your own cloud accounts, ensuring you always have access to all VMs and data. This applies to all services used to build your solution, such as AWS, Hetzner Cloud, Elastic.co, and more. Since you own the accounts, you can take over control at any time.
    {{</feature>}}

    {{<feature title="Virtual Maschines & Servers" iconClasses="fa-server solidblocks-green">}}
      Although a full-fledged container orchestration like Kubernetes has its benefits, sometimes a simpler solution based on Virtual Machines (VMs) or even bare metal can be more cost-effective, and easier to handle, maintain, and operate. If available, deploying to on-premise hardware is also an option, to better utilize already existing gear.
    {{</feature>}}


  {{</feature-row>}}

  {{<feature-row>}}

    {{<feature title="Infrastructure as Code" iconClasses="fa-code solidblocks-green">}}
      All deployed resources are described and deployed with infrastructure-as-code solutions such as Terraform, OpenTofu, and Ansible. For applications lacking an automation front-end, custom solutions can be implemented and integrated into the deployment lifecycle.
    {{</feature>}}
    
    {{<feature title="Developer experience" iconClasses="fa-play solidblocks-orange">}}
      The deployment process is designed for ease of use on developer machines and for integration into common CI/CD systems like GitHub, GitLab, Jenkins, and others. Each solution comes with a developer-friendly build system, facilitating the deployment and interaction with the deployed resources.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="Security & Data Safety">}}
  {{<feature-row>}}
    
    {{<feature title="Updates" iconClasses="fa-arrows-rotate solidblocks-orange">}}
        All components like operating systems and software packages are regularly updated. Where applicable, tools like <a href="https://github.com/renovatebot/renovate">renovate</a> are integrated into the deployment process to ensure everything is always up-to-date.
    {{</feature>}}
    
    {{< feature title="Encryption" iconClasses="fa-lock solidblocks-green">}}
      All data stored outside the cloud, such as backups, is encrypted by default to protect against accidental exposure of sensitive information.
    {{</feature>}}
    
    {{< feature title="Secret Rotation" iconClasses="fa-key solidblocks-yellow">}}
      All secrets and user credentials can be rotated at anytime to mitigate the risk of long-lived credentials that may leak over time. This also allows for a quick deprecation of existing secrets and credentials in case of a leak. 
    {{</feature>}}
    
  {{</feature-row>}}

  {{<feature-row>}}

    {{< feature title="Backups" iconClasses="fa-cloud-arrow-up solidblocks-yellow">}}
      Encrypted data backups to other clouds like AWS or GCP provide an extra layer of security for your data and reduce the blast radius in case of accidental data deletion or configuration mistakes. 
    {{</feature>}}

    {{< feature title="CVE Scans" iconClasses="fa-magnifying-glass solidblocks-green">}}
      Automated CVE scans, coupled with an always up-to-date SBOM, make it easy to discover security-critical bugs early on and to mitigate them.
    {{</feature>}}

    {{< feature title="IDP/IAM" iconClasses="fa-id-card solidblocks-orange">}}
      IDM solutions like <a href="https://www.keycloak.org">Keycloak</a> can easily be integrated to secure your application or, in combination with <a href="https://www.vaultproject.io">Hashicorp Vault</a>, to secure SSH access to your VMs with short-lived secrets.
    {{</feature>}}
    
  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="Deployment Lifecycle" >}}
  {{<feature-row>}}

    {{<feature title="Environments" iconClasses="fa-layer-group solidblocks-yellow">}}
      Multi-environment support is a first-class citizen. It can be used to support your application lifecycle and to provide different test environments.
    {{< /feature>}}
    
    {{<feature title="Bootstrapping" iconClasses="fa-terminal solidblocks-orange">}}
      Deletion and bootstrapping of environments is systematically tested to ensure there are no hidden cyclic dependencies in the infrastructure setup. This method also confirms that the code which is only executed during the initial setup continues to function correctly.
    {{</feature>}}
    
    {{<feature title="Disaster Recovery" iconClasses="fa-fire solidblocks-green">}}
      Restoring environments from backups is tested regularly and is included in both the playbooks and developer briefings. All components are designed in a way, that the entire environment can be destroyed at any time, and then fully rebuilt from the backups.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container >}}

<div class="feature-divider"></div>

{{<feature-container title="Logging & Monitoring" >}}
  {{<feature-row>}}

    {{<feature title="Logs" iconClasses="fa-server solidblocks-green">}}
      Logging platforms like Elastic.co can be used to ingest all application and VM logs and help to detect and resolve errors early on. Also haveing central logs can help to and debug bugs.
    {{</feature>}}

    {{<feature title="Metrics" iconClasses="fa-chart-line solidblocks-orange">}}
      Application and VM metrics can be gathered on analytics platforms, such as Grafana or Elastic.co. These platforms are useful for detecting and visualizing application usage, performance, long-term trends, and can help in sizing decisions.
    {{</feature>}}

    {{<feature title="Tagging" iconClasses="fa-tag solidblocks-yellow">}}
      All logs and metrics are tagged with information about the environment, service, version, etc., and are also enriched with events such as deployments. This makes it easy to correlate issues and bugs with different application versions and deployments.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="CI/CD" >}}

  {{<feature-row>}}

    {{<feature title="Deployment" iconClasses="fa-infinity solidblocks-yellow">}}
      The deployment can easily be integrated into all major CI/CD systems or into already existing application build pipelines.
    {{</feature>}}

    {{< feature title="Testing" iconClasses="fa-crow solidblocks-green">}}
      Infrastructure integration tests in the deployment process ensure that the deployment is successful. They also serve as a canary to warn when parts of the infrastructure or application are broken or in a degraded state.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}

<div class="feature-divider"></div>

{{<feature-container title="Documentation & Support" >}}

  {{<feature-row>}}

    {{<feature title="Playbooks" iconClasses="fa-list-ol solidblocks-green">}}
      Playbooks are a crucial part of every solution and provide detailed procedures for scenarios like disaster recovery or secret rotation during emergencies. They also provide information for general operation and maintenance.
    {{</feature>}}

    {{<feature title="Fire Drills" iconClasses="fa-fire-extinguisher solidblocks-orange">}}
      Regular exercises ensure that crucial steps that are seldom used still work as intended, and that developers are comfortable with executing them.
    {{</feature>}}

    {{<feature title="Support" iconClasses="fa-phone solidblocks-yellow">}}
      In the event of critical errors, such as system crashes or data loss, German and English emergency support is available via email, phone, or chat.
    {{</feature>}}

  {{</feature-row>}}
{{</feature-container>}}
