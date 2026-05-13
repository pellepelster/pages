---
title: "Solidblocks Security Update (Fragnesia)"
date: 2026-05-13T17:00:00+01:00
draft: false
---

A central aspect of the infrastructure deployed by Solidblocks cloud is the reduction of moving parts to minimize the potential attack surface. Instead of using a complex runtime involving control-planes and container schedulers, the deployed services rely on simple Debian-based VMs running plain services managed by systemd.

Despite the simplicity, security issues in the Linux kernel are sadly still an occasional thing, especially since AI-based scanners are able to reveal hard-to-find exploits faster than ever. 

The VMs created by Solidblocks cloud are ephemeral by design, all state is safely kept on dedicated storage volumes and/or external backups, so updating the VMs with the latest security patches is as easy as throwing the old VMs away and recreating them with the latest updates applied.

To make this process easy and fast, the Solidblocks Cloud CLI now also supports fetching automatic updates. To mitigate, for example, the current CVE-of-the-day [Linux Fragnesia](https://github.com/v12-security/pocs/tree/main/fragnesia) run the updater and apply the current config to your cloud.

For example the infrastructure that this blog is hosted on is defined [here](https://github.com/pellepelster/infrastructure/blob/main/pelle.yaml) and the Fragnesia mitigations were applied with

```shell
blcks update
blcks cloud apply pelle.yaml
```

{{< asciicast src="/img/blcks_update_apply.cast" theme="solarized-light" speed=4.0 >}}


For available updates, regulary check the [changelog](https://github.com/pellepelster/solidblocks/releases/)
