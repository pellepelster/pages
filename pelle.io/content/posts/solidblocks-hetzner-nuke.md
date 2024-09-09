---
title: "Solidblocks Hetzner Nuke"
date: 2024-09-08T19:00:00
draft: false
tags: ["hetzner", "solidblocks"]
---

One of the main benefits of using infrastructure-as-code is the reproducibility of your environments. No matter what happens, you can wipe everything away and rebuild from scratch. Like with many things, practice is required to ensure that the bootstrap capability is not lost over time.

Typically, you will want to regularly wipe and rebuild your test (or development) environment to prevent cyclic dependencies from creeping in, ensuring that your bootstrapping code still functions as intended.


For cloud providers like AWS there are tools like [aws-nuke](https://github.com/rebuy-de/aws-nuke) that enable you to delete all resources within one or more AWS accounts at once.

For the [Hetzner Cloud](https://www.hetzner.com/cloud/) the [Solidblocks CLI](https://pellepelster.github.io/solidblocks/cli/) now offers a similar nuke tool that helps you to wipe all resources in a cloud project. The binaries for all major platforms are available [here](https://github.com/pellepelster/solidblocks/releases/latest), and the usage pattern is similar to [aws-nuke](https://github.com/rebuy-de/aws-nuke). 

When invoked without the `--do-nuke` it will just log what resources would be deleted

```shell
$ HCLOUD_TOKEN=<hetzner api token> blcks hetzner nuke 
[blcks] running a simulated delete, add '--do-nuke' to actually delete resources
[blcks] would delete server 'test-nf88k4eak41s4r78' (52880247)
[blcks] would delete server 'hcloud-server-dkjnqarz' (52881513)
[blcks] would delete volume 'test2-nf88k4eak41s4r78' (101279889)
[blcks] would delete volume 'test1-nf88k4eak41s4r78' (101279888)
[...]
[blcks] would delete volume 'hcloud-volume-dkjnqarz-18' (101280027)
[blcks] would delete network 'hcloud-network-dkjnqarz' (10119347)
[blcks] would delete ssh key 'hcloud-ssh-key-dkjnqarz' (22910430)
[blcks] would delete certificates 'hcloud-uploaded-certificate-dkjnqarz' (1329685)
[blcks] would delete firewall 'hcloud-firewall-dkjnqarz' (1602251)
[blcks] would delete floating ip 'hcloud-floating-ip-dkjnqarz' (69258873)
[blcks] would delete load balancer 'hcloud-load-balancer-dkjnqarz' (2047136)
[blcks] would delete primary ip 'primary_ip-69256234' (69256234)
[blcks] would delete primary ip 'hcloud-primary-ip-dkjnqarz' (69258874)
[blcks] would delete primary ip 'primary_ip-69258891' (69258891)
[blcks] would delete primary ip 'primary_ip-69258892' (69258892)
[blcks] would delete placement group 'hcloud-placement-group-dkjnqarz' (386874)
```

calling it again with `--do-nuke` will actually wipe all resources

```shell
$ HCLOUD_TOKEN=<hetzner api token> blcks hetzner nuke 
hetzner nuke --do-nuke
[blcks] nuking all resources
[blcks] would delete server 'test-nf88k4eak41s4r78' (52880247)
[blcks] would delete server 'hcloud-server-dkjnqarz' (52881513)
[...]
[blcks] would delete volume 'hcloud-volume-dkjnqarz-2' (101280015)
[blcks] would delete volume 'hcloud-volume-dkjnqarz-12' (101280016)
[blcks] would delete volume 'hcloud-volume-dkjnqarz-10' (101280017)
[blcks] waiting before starting deletion, 15 seconds left...
[blcks] waiting before starting deletion, 14 seconds left...
[blcks] waiting before starting deletion, 13 seconds left...
[blcks] waiting before starting deletion, 12 seconds left...
[blcks] waiting before starting deletion, 11 seconds left...
[blcks] waiting before starting deletion, 10 seconds left...
[blcks] waiting before starting deletion, 9 seconds left...
[blcks] waiting before starting deletion, 8 seconds left...
[blcks] waiting before starting deletion, 7 seconds left...
[blcks] waiting before starting deletion, 6 seconds left...
[blcks] waiting before starting deletion, 5 seconds left...
[blcks] waiting before starting deletion, 4 seconds left...
[blcks] waiting before starting deletion, 3 seconds left...
[blcks] waiting before starting deletion, 2 seconds left...
[blcks] waiting before starting deletion, 1 seconds left...
[blcks] deleting volume 'test2-05m8qhvccqbrno0p' (101279898)
[blcks] disabling protection for volume 'test1-05m8qhvccqbrno0p' (101279899)
[blcks] deleting volume 'test1-05m8qhvccqbrno0p' (101279899)
[blcks] disabling protection for volume 'hcloud-volume-dkjnqarz-13' (101280021)
[blcks] deleting volume 'hcloud-volume-dkjnqarz-13' (101280021)
[blcks] disabling protection for volume 'hcloud-volume-dkjnqarz-15' (101280022)
[blcks] deleting volume 'hcloud-volume-dkjnqarz-15' (101280022)
[...]
[blcks] disabling protection for floating ip 'hcloud-floating-ip-dkjnqarz' (69258873)
[blcks] deleting floating ip 'hcloud-floating-ip-dkjnqarz' (69258873)
[blcks] deleting load balancer 'hcloud-load-balancer-dkjnqarz' (2047136)
[blcks] disabling protection for primary ip 'hcloud-primary-ip-dkjnqarz' (69258874)
[blcks] deleting primary ip 'hcloud-primary-ip-dkjnqarz' (69258874)
[blcks] deleting placement group 'hcloud-placement-group-dkjnqarz' (386874)
```

Look [here](https://pellepelster.github.io/solidblocks/cli/nuke/) for more information and usage details.