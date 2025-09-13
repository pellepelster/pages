---
title: "Solidblocks CLI Hetzner ASG"
date: 2025-09-13T18:00:00+01:00
---

As laid out in my previous post "[out of cloud](/posts/out-of-cloud/)" migrating applications from the
big-hyperscalers to smaller (and cheaper) alternatives sometimes comes with the challenge to figure 
out alternatives for features that may be missing in the target environment you are moving the application to.

In the past I have done a lot of AWS/GCP/Azure to Hetzner cloud migrations, and one particular feature I was
missing, especially for plain-VM deployments are the AWS Auto Scaling groups that automatically mange VMs attached
to load balancers and are capable of conducting rolling deployment updates of those severs.

Emulating them with IaC tools like Terraform/Tofu although possible tend to be a mess due to the declarative
nature of those tools, which do not pair well with a rolling update process that needs a control loop to 
diverge towards an intended state and needs to be able to cope with intermittent failures.

I added a new command to my [infrastructure cli tooling](https://pellepelster.github.io/solidblocks/cli/index.html) 
suite that can facilitate the basic rolling update feature of AWS ASGs for Hetzner load balancers.

## usage example

`blcks hetzner asg rotate` is a rolling replace for cloud servers attached to a Hetzner load balancer. All servers 
that are attached to a Hetzner load balancer provided by `--loadbalancer` will be replaced with new
servers created using the user data from `--user-data`.

So given a user data script like

**user-data.sh**
```shell
#!/usr/bin/env bash

apt-get update
apt-get -y install nginx
```

The command

```shell
blcks hetzner asg rotate --loadbalancer application1 --user-data user-data.sh --replicas 2
```

will ensure all servers attached to the load balancer `application1` are up to date, and 
that `2` replicas are ready to serve requests.

To scale up the replicas to `4` just re-run the command with the following parameters

```shell
blcks hetzner asg rotate --loadbalancer application1 --user-data user-data.sh --replicas 4
```

The commands print extensive progress information during the update process

```shell
[blcks] load balancer 'application3' has no private network, will use public ip for new servers
[blcks] hash for provided user data is '359b1b9aefeee2e27a3d7d7c6cbb5ac89747180ba3de9804643a08c133735ee4'
[blcks] inspecting load balancer 'application3'
[blcks] found 1 attached server(s) ('application3-20250913182057-0') on load balancer 'application3'
[blcks] inspecting servers for  load balancer 'application3'
[blcks] starting rollout '20250913183842' to 1 replicas, server name prefix is 'application3-20250913183842' and timeout for newly created servers is '5m'
[blcks] rotating 1 managed server(s), 1 outdated and 0 up to date
[blcks] creating server 'application3-20250913183842-0'
[blcks] waiting for 'create_server' to finish current status is 'running'
[blcks] waiting for 'create_server' to finish current status is 'running'
[blcks] waiting for 'create_server' to finish current status is 'running'
[blcks] waiting for 'create_server' to finish current status is 'running'
[blcks] waiting for 'create_server' to finish current status is 'success'
[blcks] server 'application3-20250913183842-0' has pending actions
[blcks] waiting for 'start_server' to finish current status is 'running'
[blcks] waiting for 'start_server' to finish current status is 'running'
[blcks] waiting for 'start_server' to finish current status is 'running'
[blcks] waiting for 'start_server' to finish current status is 'success'
[blcks] attaching server 'application3-20250913183842-0' to load balancer 'application3'
[blcks] waiting for 'add_target' to finish current status is 'success'
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 30s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 36s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 41s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 47s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 52s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 59s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 1m 4s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 1m 10s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 1m 15s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 1m 21s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] waiting for servers 'application3-20250913183842-0' (age: 1m 26s) to become healthy within 5m
[blcks] rotating 2 managed server(s), 1 outdated and 1 up to date
[blcks] wanted 1 healthy servers found 1 ('application3-20250913183842-0' (age: 1m 33s))
[blcks] deleting outdated managed servers: 'application3-20250913182057-0'
[blcks] deleting outdated managed server 'application3-20250913182057-0'
[blcks] waiting for 'delete_server' to finish current status is 'running'
[blcks] waiting for 'delete_server' to finish current status is 'running'
[blcks] waiting for 'delete_server' to finish current status is 'running'
[blcks] waiting for 'delete_server' to finish current status is 'running'
[blcks] waiting for 'delete_server' to finish current status is 'success'
[blcks] rollout finished
```

Look [here](https://pellepelster.github.io/solidblocks/cli/asg/index.html) for more details and usage information.