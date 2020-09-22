[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://github.com/funkygao/cp-ddd-framework/blob/master/LICENSE)
[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Code Quality: Java](https://img.shields.io/lgtm/grade/java/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/context:java)
[![Total Alerts](https://img.shields.io/lgtm/alerts/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/alerts/)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)
[![TODO](https://badgen.net/https/api.tickgit.com/badgen/github.com/funkygao/cp-ddd-framework?label=todos)](https://www.tickgit.com/browse?repo=github.com/funkygao/cp-ddd-framework)
[![Gitter](https://img.shields.io/gitter/room/cp-ddd-framework/cp-ddd-framework.svg)](https://gitter.im/cp-ddd-framework/community)

# cp-ddd-framework (Central Platform：中台)

* [What is cp-ddd-framework](#what-is-cp-ddd-framework)
   * [Current status](#current-status)
   * [Quickstart](#quickstart)
   * [What problems does it solve](#what-problems-does-it-solve)
   * [Key features](#key-features)
   * [Key abstractions](#key-abstractions)
   * [Why it stands out](#why-it-stands-out)
* [Requirements](#requirements)
* [Modules](#modules)
* [Example using cp-ddd-framework](#example-using-cp-ddd-framework)
* [Landscape of Central Platform](#landscape-of-central-platform)
* [Reference guide](#reference-guide)
* [Building from Source](#building-from-source)
* [Contribution](#contribution)
* [Licensing](#licensing)

## What is cp-ddd-framework?

cp-ddd-framework is a lightweight development framework for complex business architecture.

轻量级业务中台开发框架，以DDD思想为基础，融合业务中台核心要素，赋能业务中台建设。

融合了DDD、前中台复杂生态协作方法论，同时充分考虑组织、历史债、运维和落地成本以及风险而开发的，面向复杂业务场景架构设计，是**业务中台架构的整体解决方案**。

### Current status

目前在多个核心中台项目生产环境下使用。

**ATTENTION**：Container对Plugin的动态加载，目前仅支持一次性动态加载，热更新机制还在开发中，请不要使用。

### Quickstart

**Tip**：使用该框架，需要对`DDD`的分层架构有基本了解。

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

这14个核心抽象，勾勒出业务中台的骨架，以不变应万变。

![](http://www.plantuml.com/plantuml/svg/XLHDZziW4BtpApZk7l87h2ZQRTF3IgsqslPUZM4Ses28O9nirVQ_Tt2eOmUgtioynvkFUVWf4WGQ1olYqRWJ11Xa3qvVWY529pkm4HTOx47xYt67xVPYgFz-FUzsaiA8GXX763fGA5_sVW3Zlc8u6uKLWF0a5j-oaVYEQYHyU5onKHcDYH4_lLEfyuPdVuJMOXOiWwmSlBKjgEDQ-aMZ8qC_tZ1wUyPGYdD6TD5ujuHUxNX8scB-ajjVcqrnXnjAd_gUL-ITYOiaNxn6-qm4gX-OdLglnAQXdIndvSxgU61X30n36oc08fbdYUeR3doNy0PzW8ZNBfvROxb4KKHOye0qDyd2DKfdGlmlIMC7ertHYjfMjjZlztp-6Gd8gD-fZe_U4MjYAD7EE9rprS4f5qDt5ak5tGAC5_jYnW2a-eUyyMdkZqAiBRyoHU7s3DOjNAYKUn7tRikAwrvk5gopIyVCDofjJtTUWGY3-pSSo64GbOLl6UOrDDFmryDO8PLYQoDefFKA22lypMRtjyv6jgnPf-HQCsMoI0hFdhfQNIpFiu7IapRkimUDGImeIU3stbEDmD8v4v-LroXoRVbO5fVZvMj8yajBDT87rMDwlSGNnHCwdVxbVm00)

### Why it stands out

- Simple design and implementation
- Light weight
- Flexibility as result of total abstraction

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

使用该框架搭建`订单中台`的例子。

## Landscape of Central Platform

业务中台建设全景图。

![](doc/assets/img/landscape.png)

## Reference guide

Please go to [cp-ddd-framework Javadoc](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/).

## Building from Source

``` bash
git clone https://github.com/funkygao/cp-ddd-framework.git
cd cp-ddd-framework
mvn clean install
```

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [issue tracker](https://github.com/funkygao/cp-ddd-framework/issues).

For any question, you can use [Gitter](https://gitter.im/cp-ddd-framework/community).

## Licensing

cp-ddd-framework is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
