---
title: "Solidblocks CLI Terraform/Tofu"
date: 2025-06-09T20:00:00+01:00
---

If you are using Terraform (or Tofu nowadays) you might already have run into the chicken-and-egg issue that comes with the infrastructure state storage. To store the state you need to have some kind of container for that state to live in, assuming you don't want to store it locally which comes with its own set of issues. 

And also because you always do infrastructure as code by the book (:-)), this container should automatically be provisioned so the process is properly automated and fully reproducible.

To help with this specific use case of infrastructure setup, I added a new command to the [Solidblocks cli](https://pellepelster.github.io/solidblocks/cli/terraforn/index.html) infrastructure tooling to create a suitable private and versioned AWS S3 bucket for Terraform and Tofu state storage.

**usage**
```shell
blcks tofu backends s3 my-state-bucket
```

```shell
[blcks] creating S3 bucket 'my-state-bucket'
[blcks] public access for S3 bucket 'my-state-bucket' already disabled
[blcks] enabling versioning for bucket 'my-state-bucket'
[blcks] state configuration for created S3 bucket
---
terraform {
    backend "s3" {
        region          = "eu-central-1"
        bucket          = "my-state-bucket"
        key             = "main"
    }
}
---
```

See the [docs](https://pellepelster.github.io/solidblocks/cli/terraforn/index.html) for more options and installation information.