---
title: "Solidblocks Hetzner DNS"
date: 2024-10-02T14:00:00
draft: false
tags: ["hetzner", "solidblocks"]
---

Just in time for the public holiday in Germany, the Solidblocks infrastructure components collection got another addition. `solidblocks-dns` is a Kotlin library that lets you interact with the Hetzner DNS API to easily  manage DNS zones and records.

<!--more-->

To use it just pull in the dependency in Gradle or Maven

```groovy
// [...]

dependencies {
// [...]
implementation("de.solidblocks:infra-dns:v0.2.9")
}
```

and start creating DNS zones and records

```groovy
val api = HetznerDnsApi(System.getenv("HETZNER_DNS_API_TOKEN"))

val createdZone = api.createZone(ZoneRequest("my-new-zone.de")).getOrThrow()
println("created zone with id ${createdZone.zone.id}")

val createdRecord =
api.createRecord(RecordRequest(createdZone.zone.id, RecordType.A, "www", "192.168.0.1")).getOrThrow()
println("created record with id ${createdRecord.record.id}")
```


more information as always in the [documentation](https://pellepelster.github.io/solidblocks/hetzner/dns/index.html) or in the [Github repository](https://github.com/pellepelster/solidblocks).