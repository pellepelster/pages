---
title: "Out of cloud"
date: 2025-02-18T10:00:00+01:00
draft: true
---

This post will detail the key architectural considerations and findings of a service migration from manged Google Cloud services to a self-managed, but still cloud hosted option based on the Hetzner Cloud. The goal is to show the different considerations before attempting such an approach as well as some common pitfalls and is not intended as a ready-to-use blueprint for similar projects. 

<!--more-->

# Initial scenario

The scenario is a B2C website hosted on Google Cloud. The system consists of ~20 services and ~2 jobs executed by Google Cloud Run, a read-heavy ~300GB PostgreSQL database and some auxiliary services like Redis, MongoDB, etc. Initial starting point for the project was to reduce the monthly cloud costs, which where mostly visible for the database part. Potential target for the initial evaluation was the Hetzner Cloud offering.


## Comparing the clouds

To paraphrase a quote of unknown origin

> There are no solutions, just tradeoffs. Don't think there's a perfect tech stack.

comparing the available components of the Google and Hetzner Cloud it is quite obvious, that the latter needs some significant work to reach feature parity with the current Google setup

**missing features in the Hetzner cloud**
![GCP Overview](/img/out-of-cloud1.png)

Generally speaking the biggest tradeoff decision to make here was to weight up the additional engineering cost needed to close the gap of different abstraction levels between the clouds. As always this is not a black/white decision but depends on several considerations


* **Scalability**

    What load and in particular what load patterns need to be handled? Big traffic spikes might mean additional engineering for automatic scaling capabilities. Also the geographic distribution of the load is important, i.e. do we need to server multiple regions (Americas, Europe, Asia, ...) to lower latencies or can we get away with a single zone deployment?

* **Cost Efficiency**

    This is kind of a grey area, because of the already mentioned tradeoff between additional engineering and abstraction. On the raw pricing-per-specs level though it's a little bit easier to compare a managed Cloud SQL instance in Frankfurt with 24 vCPUs, 128Gb memory and 2TB storage comes at ~2000€/month, where in comparison a [EX130-S](https://www.hetzner.com/dedicated-rootserver/matrix-ex/) dedicated server averages at ~150€/month

* **Managed Infrastructure**

    This is also an important point to consider. Not so much for the VMs because the underlying systems are fully manged in both environments but if real hardware comes into the equation we have to consider the possibility that we might need to deal with failing disk drives, memory or basically anything else that can break on a modern system.

* **High Availability and Redundancy**

    Major cloud providers offer geographically distributed services with built-in failover, disaster recovery, and redundancy. This means your services are more resilient and have higher uptime compared to self-hosting again of course with an attached price-tag.

* **Security and Compliance**

  Cloud providers often have extensive security measures and certifications in place. They also provide tools to help meet compliance requirements like GDPR, ISO-27001, etc.

* **Flexibility**

    Cloud environments provide a wide array of services, from storage and computing power to advanced machine learning and database management. You can quickly experiment and deploy new services without heavy investment in infrastructure.

* **Automatic Software Updates and Patching**

    Cloud providers handle software maintenance, ensuring that your services are always up-to-date with the latest features and security patches, which we would need to replicate for the self-managed part of our stack in the Hetzner cloud. 

* **Observability**

  The Google cloud platform offers a tightly integrated observability stack well integrated into all services. If possible logging and metrics become even more important when we need to do parts of the operations for ourselves, so we need to find a replacement here as well.

* **Operational experience**

  The operational experience of the people that have to maintain the solution is another important consideration, because they need now need to care for more layers of the overall architecture instead of just pointing a container scheduler to a new docker image.  

* **Backup and disaster recovery**

  Especially when looking at dedicated machines we need to think about backup and disaster recover scenarios. Despite all redundancy, Raid and clustering it is still possible to lose data due to hardware failure, misconfiguration or operator error.  In a cloud scenario we would just revert to the latest backup, which is a safety net we definitly also want to have in the new solution.   

## Making Tradeoffs

When asked how the future solution should look like, its hard not to answer with the default consultant answer of "it depends", but it really depends on the individual circumstances of each project :-).


As an example lets have a look at the possible replacement for Cloud SQL in our self-hosted scenario

![MTTR/MTBF](/img/out-of-cloud2.png)


* The simplest solution is a single VM with some attached storage that hosts the database instance. Easy to deploy with Terraform, simple to debug because it's just a single machine if in doubt we can just SSH into the machine and look around. On the downside if the machine dies the database is unavailable, and we need to manually intervene to fix the issue. We are treating mean-team-to-recovery (MTTR) here for complexity to get a simpler system
* The next option would be a two-node database setup with a master and a replica following the master. This is a little bit more complex to set up operate and monitor but gives us some leverage in case the master dies, because we can then manually promote the replica to master and switch over services to the new master reducing the MTTR
* We can even take this a little bit further and use a proxy in front of the database like [pgbouncer](https://www.pgbouncer.org/) or similar to make the switchover in the failure case faster, shaving off some more time of the MTTR but trading in a little bit more complexity
* Now the next step would be a multi-node setup with automated failover, for example with [patroni](https://github.com/patroni/patroni) or similar systems, to reduce MTTR and MTBF. But again with have to pay in added technical and operational complexity
* The non-local attached storage volume for Hetzner cloud VMs are a bit limited in terms of IOPS, so if the performance requirements are a higher we might need to opt for dedicated machines do deploy the database solution, adding another level of complexity to the solution because we now also need to factor in the possibility of hardware failures in our MTTR/MTBF equation  

## A solution

After balancing pros and cons for the full stack, this is an exemplary target architecture 

![MTTR/MTBF](/img/out-of-cloud3.png)


* Infrastructure as Code
  * Terraform provisioned Hetzner cloud resources
  * manually added dedicated servers attached to the same VPC for private local traffic via [vSwitch](https://docs.hetzner.com/cloud/networks/connect-dedi-vswitch/)
  * from OS level upwards everything is provisioned using Ansible, both for dedicated servers and cloud VMs
    * big community with mature solutions for common problems readily available (updates, ssh-key distribution, firewall, etc.)
  * regular security updates also orchestrated with Ansible, triggerd by CI/CD pipelines
* Container orchestration
  * Relatively lightweight K8S implementation based on [K3S](https://k3s.io/)
    * less complex to set up and debug compared to a full-blown K8S solution
    * huge mind-share due to wide adaption helps with issues and bugs
    * Integration with Hetzner cloud resources via [Hetzner kubernetes cloud controller](https://github.com/hetznercloud/hcloud-cloud-controller-manager)
    * K8S as de-facto standard for container orchestration as of now a relatively safe bet, could make future migrations to other clouds easier
    * a lot of the needed axillary tooling is available in the form of mature operators/helm charts (redis, mongodb, ...) 
    * mature tooling and lots of documentation available for day-to-day operations
    * Cilium as network plugin for pod communication
      * decent debugging tools for network debugging and logging with [hubble](https://github.com/cilium/hubble)
      * good integration in K3S and the [Hetzner kubernetes cloud controller](https://github.com/hetznercloud/hcloud-cloud-controller-manager) for local routing in private Network
  * Database
    * high performance requirements mandate usage of dedicated machines
    * master/replica setup with pgbouncer as frontend is a manageable setup that can grow into more automated (complex) setup when operational experience grows
    * short downtimes for switchover were acceptable in this particular situation
    * switchover can be orchestrated with Ansible with only minimal needs for manual interaction
    * backup to non-Hetzner cloud S3 bucket using [pgbackrest](https://pgbackrest.org/)
      * reduces blast radius in case of accidental resource deletion
  * Observability
    * Metrics and logs from all servers collated with [metricbeat](https://www.elastic.co/de/beats/metricbeat) and [filebeat](https://www.elastic.co/de/beats/filebeat) 
      * wide range of supported collectors for components like PostgreSQL, Redis, etc. 
      * data is shipped to dedicated [elastic.co](https://elastic.co)
      * Grafana would also have been an option, but the way logs can be analyzed in Elasticsearch resembles Google Cloud logging, providing some kind of commonality
 * Operations
   * do it manually first and automate afterward to gain confidence and understanding of the whole system 
   * firedrills to gain experience and train how
     * incident look and feel in monitoring and on the servers when they are happening
     * how to fix the issue, using either intervention or IaC 
     * example drills
       * randomly delete a K3S cluster node and recover
       * delete/corrupt database data from master and restore from backup
       * delete whole environment and restore everything from backup