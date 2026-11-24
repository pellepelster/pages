---
title: "Swiss Army Knife, Part 1: Why"
date: 2026-11-02T20:00:00+01:00
tags: [ "sak" ]
---

As an infrastructure consultant, DevOps engineer, system administrator or whatever you like to call it, I take a little pride and a lot of joy in maintaining my own infrastructure for the services I need. And, like many other people working in IT, I assume, I am constantly bouncing between running everything myself and using every available app or SaaS under the sun to save me from the burden of running my own servers in my free time.

This has been fun and also a good way to get into new technologies. For example, the web server serving the bytes you are reading right now has in the past been deployed via or on

* cloud-init
* Ansible
* Salt
* Terraform
* Pulumi
* Cloudflare
* S3
* K8s
* and many more implementations I have already forgotten

The services I have been running myself, or using from other providers, were things like

* web hosting for my blog
* a todo list for me and the family
* a shared grocery app to keep track of the stuff to buy for the household
* a backup space for all laptops in the family

So pretty mundane stuff that could be regarded as commodity services nowadays. The reason for the bouncing between the different solutions was always that after the honeymoon phase was over for the next implementation, each solution had some downsides that made me swing in the other direction again.

For the self-hosted solutions, the downsides were

* the time needed for regular maintenance
  * constant updates triggered by current CVEs
* inconsistent UX across different apps
* fiddly setup of apps, leading to low acceptance in the family
* due to time constraints, tooling was always lacking
* important procedures like backup and restore were fiddly to set up and seldom tested

On the SaaS side, the issues were

* apps and websites littered with tracking, sometimes the cookie banners were showing >1000 third parties that have access to the data
* although I mostly used free or lower tier paid plans, cost was adding up and increasing over the years
* the free or cheap basic plans were constantly losing features that moved into higher tiers
* forced, half-baked AI features, no opt-in and sometimes even no opt-out
* basically no privacy, my data can more or less be used for anything the vendor or any third party wants (data mining on grocery lists seems to be a thriving business)

Common to both solutions is that they were often way too complex and versatile for the use case. Stuff like an RBAC permission system is a little bit over the top when I just want to write down some tasks I need to remember. A planet-scale DDoS protection might also be a little too much for my blog with a few thousand hits per month.

So in the vein of [XKCD #927](https://xkcd.com/927/) (yes, the one with the standards) the only way to move forward is to come up with my own solution that will settle those problems once and for all :-).

My requirements for the next solution are:

* a basic solution for my commonly needed services, like web hosting, todo lists, grocery lists, backups
* beginner-friendly UI/Ux with a CLI for power users. Both UI and CLI must have feature parity
* native apps and desktop- and mobile-friendly web apps with a consistent UI and UX
* my servers, my domain, my data
  * I want everything under a domain I own
  * data must never leave the server unencrypted, and third-party services must only be contacted with explicit consent and not automatically
  * no tracking of any kind
* installing the system must be a single, easy-to-remember (or at least easy-to-rediscover) task
* given the current influx of CVEs, the fewer moving parts, the better
* I want a UI to explore the current state of the solution, while everything stays fully automatable via a CLI
* patches and updates must be applied constantly, without any need for manual intervention
* no local state: the running system is the source of truth; if local state is needed, it must be secure by default and ephemeral

In the next posts I will show the solution I came up with to meet these requirements. The provisional name, until I find something better, is Swiss Army Knife. Look here for all [posts](/tags/sak/) of the series and here for the [source code and documentation](https://github.com/pellepelster/swiss-army-knife).
