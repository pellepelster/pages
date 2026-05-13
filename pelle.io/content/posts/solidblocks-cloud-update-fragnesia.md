---
title: "Solidblocks Cloud S3"
date: 2026-05-11T19:00:00+01:00
draft: false
---

Although AWS S3 buckets are as stable and easy as it gets, sometimes you might not want to have an AWS dependency at all, or you expect a lot of egress traffic, making it a potentially costly choice.

Luckily, a lot of alternative implementations exist, one of them is [Garage](https://garagehq.deuxfleurs.fr/), offering a performant open-source option for self-hosting.

Garage also offers public buckets, making it a nice option to host a static website that can be deployed with any S3-compatible software and is now also an available service in [Solidblocks Cloud](https://pellepelster.github.io/solidblocks/cloud/index.html).

In the good tradition of dogfooding your own software, in fact, this blog is hosted on a Garage S3 service with a publicly exposed bucket deployed via Solidblocks Cloud, the source looks like this:

**pelle.yaml**
```yaml
# yaml-language-server: $schema=https://solidblocks.de/blcks-cloud.schema.json
---
name: pelle
root_domain: pelle.io

providers:
  - type: pass
  - type: ssh_key
    private_key: ~/.ssh/pelle.io.ed25519.key
  - type: hcloud
  - type: backup_local

services:
  - type: s3
    name: public
    buckets:
      - name: pelle.io
        public_access: true
        public_access_domains:
            - pelle.io
        access_keys:
          - name: "admin"
            owner: true
            read: true
            write: true
      - name: solidblocks.de
        public_access: true
        public_access_domains:
            - solidblocks.de
        access_keys:
          - name: "admin"
            owner: true
            read: true
            write: true
```

that when rolled out via 

```shell
blcks cloud apply pelle.yaml
```

{{< asciicast src="/img/pelle_s3.cast" theme="solarized-light" speed=4.0 >}}


rolls out the S3 service, and also creates two publicly available S3 buckets, as well as the appropriate DNS entries and provides you with some quickstart commands to start using your freshly deployed S3 service. See the [documentation](https://pellepelster.github.io/solidblocks/cloud/configuration/index.html#s3) for more information.