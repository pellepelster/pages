---
title: "PostgreSQL performance debugging part 1"
date: 2024-04-25T12:00:00+01:00
draft: true
tags: [ "postgresql" ]
---

[Solidblocks RDS PostgreSQL](https://pellepelster.github.io/solidblocks/hetzner/rds/index.html) is a ready to use, all-batteries included module for deploying PostgreSQL databases to the [Hetzner cloud](https://cloud.hetzner.com). It is part of the [Solidblocks](https://pellepelster.github.io/solidblocks/) library, which is a collection of reusable components for infrastructure operation, automation and developer experience.

And it has an performance issue.

The typical uses case until now was a simple and cheap PostgreSQL database for smaller workloads, that do not see a lot of traffic. Recently I got some feedback on unexplainable performance drops during elevated load situations. This post is the first part of the journey to debug and resolve this problem.

## Reproduce

The first step in solving this issue is to create a reproducible environment that can be used to debug the issue, and also to verify the fix if one is found. 

Because we will likely re-run and re-create the reproduce countless times, it makes sense to invest some time into automating it. Not only will this help us with running the tests in different configurations, but this also creates the opportunity to throw away and re-create everything from scratch in case we run into in an dead-end during debugging and xxx along the way. 

### Environment

Since [Solidblocks RDS PostgreSQL](https://pellepelster.github.io/solidblocks/hetzner/rds/index.html) is a Terraform module, we will of course use Terraform to create the test environment. In our case this can be done with just a simple module invocation to deploy the database.

<!-- insertSnippet[postgres-performance-debugging-postgresql] -->
```Bash
module "postgresql" {
  source  = "pellepelster/solidblocks-rds-postgresql/hcloud"
  version = "0.2.5"

  name     = "postgresql"
  location = var.location
  ssh_keys = [hcloud_ssh_key.ssh_client_identity.id]

  server_type = var.instance_type

  backup_volume = hcloud_volume.backup.id
  data_volume   = hcloud_volume.data.id

  public_net_ipv4_enabled = true

  databases = [
    { id : "pgbench", user : "pgbench", password : "pgbench" }
  ]
}
```
<!-- /insertSnippet -->

#### Variables, parameters and scenarios

To be able to verify different approaches when solving the performance it makes sense to have configuration options for the key configurations of the environment that can be parametrized from the outside to test different theories. In our case the `instance_type` type is a good candidate that we might need to tune to test the database on different machine sizes. We can externalize this with a Terraform variable definition: 

<!-- insertSnippet[postgres-performance-debugging_instance_type] -->
```Bash
variable "instance_type" {
  type    = string
}
```
<!-- /insertSnippet -->

With more and more parameters (and this will inevitably happen) the next problem we face is how we can combine different parameters into scenarios that we can re-run anytime we want.
To not go overboard with a too complex solution we will use a simple file with environment variable declarations:

<!-- insertSnippet[postgres-performance-scenario1] -->
```Bash
POSTGRESQL_INSTANCE_SIZE=ccx13
PGBENCH_INIT_SCALE=1000
PGBENCH_TIME=900
PGBENCH_AGGREGATION_INTERVAL=1
```
<!-- /insertSnippet -->

This file can be parsed and exported with a `load_scenario` function that is called before the provisioning/testing code is started. To avoid confusion it is also a good idea to print the scenario configuration for the current run.  Inserting dividers (`divider_[bold|thin]`) help to structure the console output and makes it easier for the developer to find the important parts in the log stream.

<!-- insertSnippet[postgres-performance-load_scenario] -->
```Bash
function load_scenario() {
  local scenario="${1:-}"

  if [[ -z "${scenario}" ]]; then
    echo "no scenario provided"
    exit 1
  fi

  if [[ ! -f "${DIR}/scenarios/${scenario}" ]]; then
    echo "scenario '${scenario}' not found"
    exit 1
  fi

  divider_bold
  echo "loading scenario '${1:-}'"
  export $(grep -v '^#' "${DIR}/scenarios/${1:-}" | xargs)
  divider_thin
  echo "postgresql instance type: ${POSTGRESQL_INSTANCE_SIZE}"
  echo "pgbench init scale: ${PGBENCH_INIT_SCALE}"
  echo "pgbench run time: ${PGBENCH_TIME}"
  echo "pgbench aggregation interval: ${PGBENCH_AGGREGATION_INTERVAL}"
  divider_bold
}
```
<!-- /insertSnippet -->

Put together for the deploy task like this

<!-- insertSnippet[postgres-performance-task_hetzner_deploy] -->
<!-- /insertSnippet -->

We can now deploy various scenarios like this:

```shell
./do hetzner-deploy scenario1

==================================================================================================
loading scenario 'scenario1'
--------------------------------------------------------------------------------------------------
postgresql instance type: ccx13
pgbench init scale: 1000
pgbench run time: 900
pgbench aggregation interval: 1
==================================================================================================

==================================================================================================
deploying postgresql database with instance size 'ccx13'
--------------------------------------------------------------------------------------------------

Initializing the backend...
Upgrading modules...
Downloading registry.terraform.io/pellepelster/solidblocks-rds-postgresql/hcloud 0.2.5 for postgresql...
- postgresql in .terraform/modules/postgresql

Initializing provider plugins...
- Finding latest version of hashicorp/template...
- Finding latest version of hashicorp/local...
- Finding latest version of hashicorp/tls...
- Finding hashicorp/http versions matching ">= 3.3.0"...
- Finding hetznercloud/hcloud versions matching ">= 1.38.2, 1.46.1"...

[...]
```

### Loaddriver

Now that we have a environment to test we need a load driver to create some load on the database. Luckily for us with `pgbench`, PostgreSQL already contains a nice tool to create database load.

> pgbench is a simple program for running benchmark tests on PostgreSQL. It runs the same sequence of SQL commands over and over, possibly in multiple concurrent database sessions, and then calculates the average transaction rate (transactions per second). By default, pgbench tests a scenario that is loosely based on TPC-B, involving five SELECT, UPDATE, and INSERT commands per transaction.

A load scenario using [pgbench](https://www.postgresql.org/docs/current/pgbench.html) is deployed in two steps, first the database is initialized with a configurable amount of data, then a variable number of workers are spawned that will open PostgrSQL connections and run the aforementioned SQL statements. 

#### Initialize the database

### WIP

