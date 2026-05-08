---
title: "Solidblocks Cloud Operations"
date: 2026-05-07T20:00:00+01:00
draft: false
---

April 2026 has not been kind to someone who is responsible for maintaining and securing online services, [Copy Fail](https://copy.fail/) and [dirtyfrag](https://github.com/V4bel/dirtyfrag/tree/master) kept everyone busy with updating machines and making sure no data was lost or exfiltrated. 

Environments provisioned with [Solidblocks Cloud](https://pellepelster.github.io/solidblocks/cloud/index.html) are always kept up to date, using Debian's [unattended-upgrade](https://wiki.debian.org/PeriodicUpdates) feature. While this works nicely to keep software packages up-to-date, a kernel update still requires a reboot. 

This process is now automatically available in the Solidblocks cloud CLI.

The command

```shell
blcks cloud status cloud1.yaml
```

provides an overview of all deployed servers, and any pending kernel updates.

<!--more-->

**see it in action**

{{< asciicast src="/img/blcks_status.cast" theme="solarized-light" speed=4.0 >}}

If a kernel update is pending, the update can be applied by running

```shell
blcks cloud maintenance cloud1.yaml
```

{{< asciicast src="/img/blcks_maintenance.cast" theme="solarized-light" speed=4.0 >}}


which will reboot all affected servers and make sure everything is up-to-date.