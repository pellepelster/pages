---
title: "Handling secrets in Gradle"
date: 2025-02-15T20:00:00+01:00
draft: true
---

If you are ready for another instance of me rambling about developer experience, this post will detail a small pattern to ensure that secrets that are needed for unit/integration tests are readily available locally and in CI, without the need to manually set up our development environment. 

<!--more-->



`hetzner/cloud/api_token` -> `HETZNER_CLOUD_API_TOKEN` 