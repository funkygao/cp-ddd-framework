# cp-ddd-framework (Central Platform：中台)

![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)
[![Code Quality: Java](https://img.shields.io/lgtm/grade/java/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/alerts/)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)

* [What is cp-ddd-framework](#what-is-cp-ddd-framework)
   * [What problems does it solve](#what-problems-does-it-solve)
   * [Key features](#key-features)
   * [Key abstractions](#key-abstractions)
* [Requirements](#requirements)
* [Modules](#modules)
* [Example using cp-ddd-framework](#example-using-cp-ddd-framework)
* [Landscape of Central Platform](#landscape-of-central-platform)
* [Reference guide](#reference-guide)
* [Licensing](#licensing)

## What is cp-ddd-framework?

cp-ddd-framework is a lightweight development framework for complex business architecture.

轻量级业务中台开发框架，以DDD思想为基础，融合业务中台核心要素，赋能业务中台建设。

### What problems does it solve

- born for biz extension, the OCP
- help evolve the system without corruption
- 业务逻辑的个性化：uncertain biz logic
- 业务流程的个性化：uncertain biz flow
- 业务模型的个性化：uncertain biz model
- 业务前台与中台如何协同：synergy between BP and CP teams
- 如何让中台架构特色的DDD稳稳当当落地：DDD seamlessly landing in code
- 如何降低系统实现的复杂度：reduce entropy of the system
- 普通研发如何编写出优雅的业务代码：low threshold for average developer to build elegant biz code
- and more

### Key features

- layered extension point
- model schema extension
- business flows orchestration
- dynamic loading business extension jar
- isolation of business runtime
- best practice of DDD
- and more

### Key abstractions

![](http://www.plantuml.com/plantuml/svg/XLJBZXCn4BpxAonnsJta1w8esaTos0CIMhYZ7hkJiUAnetPFiWZuTtec1iPh8svnLqtreqfvI0p4u-1LEhbm1e91zn3ibHmp1iqquWBgm9oE5CTWdwEFf3_iz_kdtVE2aNgmyUh2SREvEkw_tjxRxKt9uu4rHqtksBEsZj2mYq4f5nZf00RroouEuC9df8ips00OpshnMwyrlg4P6U_k2pOQPt5YfFqRrpglU8adw3ncmReeoXIzdtOMwKf0L34acNu9lLWCxFZRAwReBqXBx6Eqw1-PmVI3iAQf6hLvdxEN1jAqZHC6YoGmT8a93Eky9felECYtZ7-X9qXu6_YnSrv6KrL598_8V_OHuHRbu2ZzbsJn0AClQDLiXMExtKxUtnC3E_DJcWIRrThWh2RGEt_drhamuvKJmXY0JN-NxPilUwzKcJUz-R5QBQCspSeOR16vJ2eZKx9hrTg9jNPo4PoCJKgzZrNkKkvPDAZOLSQMNzGdkNtp2PYHmhzXue2a6dPl11QBfcYK1ogHrchjS_XeWB71Nwssl-TSwquqsvGyQwQe_1JdJRIeia1-rWgvtWYiQ9UPY6HwJ4mm_tizOB3Zu7y1)

## Requirements

Requires Java 1.8+ and Spring 4.3.12.RELEASE or later.

## Modules

### cp-ddd-spec

Specification of the framework.

### cp-ddd-runtime

Runtime implementation of the framework.

### cp-ddd-enforce

Enforce expected evolvement of the business architecture based upon ArchUnit.

## Example using cp-ddd-framework

See the [cp-ddd-example](cp-ddd-example).

完整的使用该框架搭建`订单中台`的例子。

## Landscape of Central Platform

![](doc/assets/img/landscape.png)

## Reference guide

Please go to [cp-ddd-framework Javadoc](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/).

## Licensing

cp-ddd-framework is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
