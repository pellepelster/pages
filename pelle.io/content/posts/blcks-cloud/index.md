---
title: Solidblocks managed Cloud
date: 2026-09-16T20:00:00+01:00
draft: true
build:
  publishResources: false
---

<sup><sub>(warning: this post is an advertisement disguised as a profound reflection on IT infrastructure projects)</sub></sup>

I spent most of my time in IT on the fine line between software development and infrastructure operations. Not surprising even though each project is individual, when it comes to deployment and operations there are a lot of common challenges and questions that all projects share:

* **Backup and disaster recovery**  *I not only want my data backed up, I also want to make sure disaster recovery is regular tested*
* **Security** *Everything must be protected by strong encryption, rotating credentials should be a non-event. Everyone, people and machines, only gets the access they need, and removing someone's access is immediate and complete.*
* **Monitoring** *I need solid monitoring for my system landscape to be able to detect problems early on before they get serious*
* **Data safety** *Everything must be encrypted by default, even I somehow lose my data I want to be sure no one can read it*
* **Reproducibility** *I want to be able te reliable reproduce a consistent system state at every time*
* **High availability** *A single failing server or component does not take my whole system down.*
* **Black start capability** *If everything goes wrong I want to be able to restart my system from zero and continue working*
* **Environments** *I want multiple versions of my system to evaluate and test changes and new features before they go live*
* **Audit logging** *I always want to know what's going on in my system*
* **Upgrades and supply chain monitoring** *All involved software must be monitored, and patched with the latest security fixes. Major upgrades of the OS, database and runtime should be planned routine work, not a migration project.*
* **Debugging** *I want to be able to debug issues in my live system in case I can not reproduce them locally*
* **Sovereignty & Compliance** *I need to know where my data physically lives and can prove it. If needed I want to be able to leave and move on*
* **Alerting and incident response** *When something breaks, the right person needs enough context and tools to resolve the issue.*
* **Cost transparency** *I always want to know what my system costs and what drives that cost.*
* **Capacity & scaling** *I see early when I'm running out of resources and have room to grow without refactoring the whole system*
* **Documentation & runbooks** *I need documentation and tooling to support and maintain the system.*

As always the answer to all those questions is a clear and straightforward "It depends". Context is important here, when your project setup consists of a 250 microservice system landscape with a 8 person SRE team in the background, Kubernetes might look like a fitting solution. But when your operations team is called Steve and Steve also needs to maintain the printers and the coffee machine, Kubernetes might be a little bit too much to run your custom PHP based contact formular on. 

It's a tradeoff: When your applications handles 24/7 global multi-million dollar transactions you have a different view on availability compared to your local machine shop who only works 9 till 5 anyway.

I spent the last year consolidating all those bits and pieces that I developed over time as an answer to all those questions into a consolidated product

Solidblocks Managed Cloud is built on a few core principles

* Simplicity over flexibility. For smaller projects with more modest requirements a lot of complexity can be shed without compromising on security and availability. Solidblocks is based on pure virtual machines and services started via systemD, no container scheduler no controls planes the VMs can run standalone and easy to understand and maintain by a human if needed
* Integrated workloads. The manged services are tightly integrated into the monitoring and metrics solution. When you deploy a database, metrics like I/O or cache hit ration are automatically configured. For JVM based workload information like the garbage collector timings are available without the need for manual configuration
* Backup & Disaster recovery. All your data is automatically backed up and encrypted in multiple external regions. Restore and complete disaster recovery is regular tested.
* Environments and Versioning are a first class citizen. Your managed services are versioned and can be deployed to multiple environments in different configurations. 
* Developer tooling. Ergonomic developer tooling makes working with your services and debugging them as easy as calling a cli command to open a tunnel to your database.
* Security and compliance. All public endpoints are encrypted by default, VMs are updated daily and monitored for CVEs.

{{< screenshot-carousel dir="screenshots" >}}
