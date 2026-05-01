---
title: "Solidblocks Cloud Github"
date: 2026-04-29T20:00:00+01:00
draft: false
---

Most of the projects I work on nowadays are hosted on GitHub. And since many of those projects are infrastructure related, a lot of the GitHub Actions based CI runs are long-running tests verifying vital aspects like provisioning and disaster recovery. Due to the slow nature of infrastructure tests when compared to unit tests, those runs burn a considerable amount of action runner time.

When using the second-smallest runner SKU `actions_linux` (4 CPU/16 GB) that clocks in at 0.0513 €/hour, which for the larger projects where at least one runner is crunching away at jobs all the time comes in at around ~222 €/month.

A comparable Hetzner cloud instance like the `CCX23` (4 CPU/16 GB) comes at a price of 32.00 €/month, making it a nice option to save some cloud spending.

To make the usage of those cheaper self-hosted runner options as easy as possible, I added GitHub as a new provider and service type to my [Solidblocks Cloud CLI](https://pellepelster.github.io/solidblocks/cloud/index.html), making the setup of runners as easy as running `blcks cloud apply github.yaml`:

**github.yaml**
```yaml
# yaml-language-server: $schema=https://solidblocks.de/blcks-cloud.schema.json
---
name: github

providers:
  - type: pass # use pass password manager for secrets
  - type: ssh_key
    private_key: ~/.ssh/ed25519.key # ssh key for VM provisioning and later access
  - type: hcloud # use Hetzner cloud to create GitHub runner VMs
    default-instance-type: ccx23
  - type: github
    github_url: https://github.com/pellepelster/solidblocks # project to attach the runner to

services:
  - type: github_runner
    name: blcks-runner
    allow_sudo: true # allow sudo inside actions shell commands
    scale: 2 # how many runners to provision
    packages: # extra packages to install
      - python3-venv
      - zip
      - unzip
    labels: # labels for runner selection in GitHub workflows
      - self-hosted
      - ubuntu-24.04
```


See the [documentation](https://pellepelster.github.io/solidblocks/cloud/configuration/index.html#github-runner) for more information.