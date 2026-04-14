---
title: "Solidblocks Cloud"
date: 2026-04-13T20:00:00+01:00
draft: false
---

For let's say "geopolitical reasons" (I've always wanted to justify something with geopolitical reasons) I have been involved in quite a few Hyperscaler exits in the past year. While it is true that it is hard to find an exact match for services like AWS in Europe an important learning during those migrations was, that you do not always need globally-redundant-hockey-stick-growth-proof-scaled services.

The loss of convenience when leaving the managed services from the big Hyperscalers can sometimes be absorbed by simpler setups that are more focused on the really important parts of your operations.

For those scenarios where you just need to run a handful of services with a plain database backend, I reworked the different components I have already published in a new tool called Solidblocks Cloud. A standalone CLI that offers managed services on plain VMs, with automatic backup, SSL certificates and secret handling.

**see it in action**
{{< asciicast src="/img/blcks_quickstart.cast" >}}

Look here for more information and [demos](https://pellepelster.github.io/solidblocks/cloud/index.html) or [here](https://pellepelster.github.io/solidblocks/cloud/design/index.html) for internal details. Some future ideas are on this rough [roadmap](https://pellepelster.github.io/solidblocks/cloud/roadmap/index.html). If you are interested in using it, I am happy to hear from you.

Of course everything is Open-Source :-)