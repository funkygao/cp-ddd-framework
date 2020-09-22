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
* [FAQ](#faq)
* [Licensing](#licensing)

## What is cp-ddd-framework?

cp-ddd-framework is a lightweight development framework for complex business architecture.

轻量级业务中台开发框架，以DDD思想为基础，融合业务中台核心要素，赋能业务中台建设。

融合了DDD、前中台复杂生态协作方法论，同时充分考虑组织、历史债、运维和落地成本以及风险而开发的，面向复杂业务场景架构设计，是**业务中台架构的整体解决方案**。

### Current status

目前在多个复杂的中台项目生产环境下使用近一年时间。

**ATTENTION**：Container对Plugin的动态加载，目前仅支持一次性动态加载，热更新机制还在积极开发中，请暂时不要使用。Plugin hotswap mechanism is not production ready yet, please don't use it.

### Quickstart

**Tip**：使用该框架，需要对`DDD`的分层架构有基本了解：该框架面向的是`DDD`的domain层开发，即面向业务沉淀层的开发框架。

`IDomainService`，领域服务，在`DDD`里是facade，完成一个完整的业务活动，例如接单。

接单在订单履约系统里，是非常复杂的过程，包括：服务产品校验，客户校验，供应商校验，店铺校验，承运商校验，增值服务校验，价格校验，促销规则校验，券抵扣，订单商品项校验，冷链的温层校验，目标仓寻源，承运商分单，库存预占，订单拆分，预分拣，地址解析等数十个(复杂场景甚至百个)的步骤，对应`IDomainStep`：一个业务活动是由多个步骤组成的。步骤，相当于隐藏业务细节而把业务活动进行拆解的抽象。

不同的业务场景下，步骤的顺序不同，步骤项不同，例如：B2B场景，接单是由步骤(A, B, D, F)完成的，而B2C场景，接单是由步骤(C, D, E, M, N)组成的。`IDomainStep`就是一种可以被编排的领域服务。

步骤的编排，有些是可以预先计算的，有些是完全动态决定的，例如：在D步骤需要调用预分拣API，根据返回结果才能决定后续步骤。步骤执行过程中，可能抛出异常，为了保证一致性，之前已经成功执行的步骤需要回滚，实现了`IDomainStep`的子类`IDomainRevokableStep`就需要实现业务回滚操作。这些机制，是模板方法类`StepsExecTemplate`实现的。

如何实现不同场景下对步骤的不同编排？同一个业务语义，不同场景的执行逻辑可能不同，例如：库存预占，有的商家要求零库存预占，有的要求缺量出库，有的要求预占失败要有冲正动作，有的要求预占成功后给商家回传状态等等。

这引出了`IDomainExtension`，扩展点：业务语义确定，但不同业务场景执行逻辑不同的业务功能点，即业务的多态。定义一个扩展点，不同业务场景有不同的实现。

这里，扩展点机制，是分层的：
- IDomainExtension，最基础的扩展点，解决业务逻辑的不确定性
- IDecideStepsExt，步骤编排扩展点，解决业务流程的不确定性
- IModelAttachmentExt，解决业务模型的不确定性，可以简单理解为如何解决多业务场景下数据库字段的问题

扩展点机制，实现了业务多态，但运行时一项业务到底是哪一个扩展点的实现来执行？这引出了`IIdentityResolver`：业务身份解析器，根据`IDomainModel`判断该业务是否属于自己，同时与某一个扩展点实现进行绑定，从而完成扩展点的路由机制。

这里的业务身份是完全动态的，支持任意场景，满足业务不确定性的需求，而不是基于经验进行建模编码的机制。

`IIdentityResolver`有2个机制：

- `Pattern`：中台部门内部的个性化
- `Partner`：前台部门的个性化，来扩展中台能力

本质上，`IIdentityResolver`，相当于把之前散落在各处的某个业务逻辑的**if**判断条件进行收敛，使得这些业务判断显式化，有形化，并有了个名字(统一语言)`；IDomainExtension`，相当于把**if**后面的code block显式化，有形化，并可以进行组织分工：前台部门开发前台的逻辑，中台部门开发中台的逻辑，协同建设一套企业级能力复用平台。

什么是业务场景？在中台下，它是不确定的。它可能是任意维度，我们想象不到的维度。

即使凭经验定义了一些维度，在2B业务下也不堪一击，很难保证它的稳定。下面列举一些业务场景的维度：

- 冷链的订单
- 大件的订单/中小件订单
- 某个商家的订单
- 某个行业的订单
- 出库仓库类型
- 出库时是否越库
- 配送是是否自提，到哪里自提
- 业务模式
- 订单项是否包含违禁品
- etc

以上，是该框架的最基本逻辑。

更详细的内容，可以参考：
- [单元测试用例](cp-ddd-test)
- [示例：订单履约中台](cp-ddd-example)
- [javadoc](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/)
- source code

需要提醒的是，这是个**业务中台**的**开发框架**，它勾勒出了业务中台的骨架，如何建设业务中台，研发拿它就知道如何组织自己的代码，遇到个性化业务如何解决，如何让业务开发变得优雅。但它无法替代业务梳理、业务抽象、业务建模。It is not silver bullet.

此外，这也不是一个万能的业务开发框架，它主要针对的是复杂、个性化特征明显、重业务逻辑的目标系统建设。如果你的系统很简单，CRUD特征明显，不建议使用。

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

## FAQ

## Licensing

cp-ddd-framework is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
