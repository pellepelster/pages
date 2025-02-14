---
title: "using borg 2.x with Hetzner object storage"
date: 2025-02-10T20:00:00+01:00
---

When selecting a tech stack for my personal backup needs, I aim for the most boring but still supported software that is available. When it comes to data safety and resilience against disk failures and other disasters, the last thing I want is to discover that the shiny new feature that was just introduced in my backup solution ate all my data and saved `0s` all the time.

I have been a heavy user of [borgbackup](https://www.borgbackup.org/) for nearly a decade now, where it flawlessly worked in the background without any fuzz. The only downside for new users adopting `borgbackup`, can be the rather complicated setup of a remote backup repository which is a little finicky to say the least, this is an [example](https://community.hetzner.com/tutorials/install-and-configure-borgbackup) for the Hetzner storage box solution 

Luckily with the new 2.x version, [rclone](https://rclone.org/) was added as a new repository backend, adding a lot of [new options](https://rclone.org/#providers) where backup repositories can be stored. 

In particular S3 is now also an option for remote storage, which together with the new [object storage](https://www.hetzner.com/de/storage/object-storage/) offering from Hetzner, offers a nice and cheap option to store your backups without using the usual suspects like AWS or Cloudflare.

This post outlines the steps you need to do to test-drive this new feature while also giving a rough overview of the general `borgbackup` usage.

<!--more-->

## Prerequisites

Since borg 2.x is still in beta (2.0.0b11 when this post was written) you will most likely need to grab a binary from [here](https://github.com/borgbackup/borg/releases) and add it to the `$PATH` of your platform. The new version also needs at least rclone v1.57.0, which you can get from [here](https://rclone.org/downloads/) in case your package manager does not offer it.

## Disclaimer

Keep in mind that borg 2.x is still in beta, I would recommend to name the binary `borg2`, and keep any previous installation intact an active until all bugs are ironed out, never trust a .0 release :-). 

## Create object storage bucket

Before we can start we need a bucket where the backups can be stored. Create one using the [cloud console](https://console.hetzner.cloud/) and generate credentials to use for `rclone`/`borg`.

## Configure rclone

The first step is to tell `rclone` how to reach and authenticate against the Hetzner object storage bucket. Depending on your buckets location (`fsn1`, `nbg1`, ...) add a config entry to `~/.config/rclone/rclone.conf`.

**Example config for a bucket in nbg1**
````properties
[hetzner-nbg1]
type = s3
provider = Other
env_auth = true
access_key_id = <access_key_id>
secret_access_key = <secret_access_key>
endpoint = nbg1.your-objectstorage.com
acl = private
region = nbg1
````

you can verify the setup with an `rclone ls` which will throw an error when it can not list the buckets content.

```shell
rclone ls hetzner-nbg1:<bucket_name>
```

## Create the backup repository

Before we can start to run an actual backups, the backup repository needs to be initialized. Apart from setting up the basic folder structure an important part of this step is, to choose and activate an appropriate encryption configuration for the repository. In general there are two options, where the main difference is where the secret key will be located. Both variants use a passphrase to encrypt protect your data, look [here](https://borgbackup.readthedocs.io/en/2.0.0b11/usage/repo-create.html#choosing-an-encryption-mode) for a more in depth description of the available options.  

**create a new repository using the recommend encryption algorithm**
```shell
borg2 repo-create --repo rclone:hetzner-nbg1:<bucket_name>/<repository_name> --encryption repokey-blake2-chacha20-poly1305
```

## Run a backup

Now that the repository is initialized, run a backup for the precious data we want to protect 

**run backup**
```shell
borg2 create --repo rclone:hetzner-nbg1:<bucket_name>/<repository_name> $(date +%Y%m%d%H%M%S) /local/path/to/backup
```

## Verify backup

An often overlooked, but nevertheless very import part of any backup strategy is to verify the backup and practice the restore. We can view some interesting statistics about the backups in the repository with `borg2 info`

**show information for all backups in the repository**
```shell
borg2 info --repo rclone:hetzner-nbg1:<bucket_name>/<repository_name>             
Archive name: 20250213190433
Archive fingerprint: 13c5f785f754499a3dff1e271f5a8232679ca9d106ebda7bb3582a7a115eab20
Comment: 
Hostname: fry
Username: pelle
Tags: 
Time (start): Thu, 2025-02-13 19:04:44 +0100
Time (end): Thu, 2025-02-13 19:24:12 +0100
Duration: 24 minutes
Command line: borg2 create --repo rclone:hetzner-nbg1:pelle-backup/<bucket_name> 20250213190433 /local/path/to/backup
Number of files: 134325
Original size: 34 gB
```

But the real deal is to actually run a restore and verify that all expected data can be restored

**restore all files from a specific archive to the current directory**
```shell
borg2 extract --repo rclone:hetzner-nbg1:pelle-backup/tmp 20250213190433 /
```

I would recommend running a restore drill every few months, to be sure that it works when it is really needed.