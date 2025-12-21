---
title: "Web S3 Docker"
date: 2025-12-20T20:00:00+01:00
---

A common obstacle I encounter when building deployments outside the typical hyperscalers like AWS or Azure is the issue where to store the initial artifacts needed to bootstrap the new deployment. Typical artifacts here are static files, like a Maven repositories, executables for services, Terraform state, Docker images or basically everything else that can be served via HTTP.

While for many uses-cases serving static data via S3 buckets is the best way to go, doing so in AWS may not always be the best option due to complicated account setup, or costly egress traffic when the data is accessed from the internet.

To solve this issues for me (and maybe others), I built a ready to use Terraform module that sets up a  S3 repository that can serve publicly accessible content on Hetzner, leveraging the rather generous egress traffic for Hetzner cloud VMs.

It supports anonymous and private access using auto-generated or pre-defined credentials for accessing static files hosted on S3 or in a docker registry.

**usage example**
```terraform
module "web-s3-docker" {
  source   = "https://github.com/pellepelster/solidblocks/releases/download/v0.4.8/terraform-hcloud-blcks-web-s3-docker-v0.4.8.zip"
  name     = "server1"
  dns_zone = "blcks-test.de"

  ssh_keys = [hcloud_ssh_key.ssh_key1.id]

  s3_buckets = [
    {
      name                     = "bucket1",
      web_access_public_enable = true,
      web_access_domains       = ["static.blcks-test.de"]
    }
  ]

}
```

The above example creates a S3 server, with n bucket `bucket1`, where all files pushed to that bucket are available under `https://static.blcks-test.de`.

Credentials as well as the endpoints for S3 are auto-generated and can be read from the Terraform output and used with any S3 compatible tooling, like for example `s3cmd`

```shell
export S3_HOST=$(terraform output -raw s3_host)
export ACCESS_KEY=$(terraform output -raw s3_host | jq '.[0].owner_key_id')
export SECRET_KEY=$(terraform output -raw s3_host | jq '.[0].owner_secret_key')

s3cmd --host-bucket ${S3_HOST} --host ${S3_HOST} --access_key ${ACCESS_KEY} --secret_key ${SECRET_KEY} put test.txt s3://bucket1
```

For a full usage example, as well as an example on how to use the Docker registry please have a look at the [documentation](https://pellepelster.github.io/solidblocks/hetzner/web-s3-docker/index.html).