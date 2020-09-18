[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://github.com/funkygao/cp-ddd-framework/blob/master/LICENSE)
[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Code Quality: Java](https://img.shields.io/lgtm/grade/java/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/context:java)
[![Total Alerts](https://img.shields.io/lgtm/alerts/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/alerts/)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)
[![TODO](https://badgen.net/https/api.tickgit.com/badgen/github.com/funkygao/cp-ddd-framework?label=todos)](https://www.tickgit.com/browse?repo=github.com/funkygao/cp-ddd-framework)
[![Gitter](https://img.shields.io/gitter/room/cp-ddd-framework/cp-ddd-framework.svg)](https://gitter.im/cp-ddd-framework/community)

# cp-ddd-framework (Central Platform：中台)

* [What is cp-ddd-framework](#what-is-cp-ddd-framework)
   * [What problems does it solve](#what-problems-does-it-solve)
   * [Key features](#key-features)
   * [Key abstractions](#key-abstractions)
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

融合了DDD、前中台复杂生态协作方法论，同时充分考虑组织、历史债、运维和落地成本以及风险而开发的，是**业务中台架构的整体解决方案**。

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

![](http://www.plantuml.com/plantuml/svg/XLJ1ZXen3BtdAtA40p-04DfjwM4FbQfjxvKdyJ0HcGGv1hPg--zrc8XEDbFrDibxyNks7pmb1kBBw5KwkN06Wb4F4EoD73C6pNJ16PG6iF7cmd6tkpaUla_d_NxYUUnPSzJaZWDhwmWDknYKSe6HUZ2eNmvn11U-8bsTmGM0yQncttghyGtDXN6rhjXedCNCI9_UkAtpYjTuWivZATXUIcMAtdTWJbNfmfMg69AudqCl5WCx_l6AAVehqXpx72twPsOmmoYix6hXxjxFsOk0Y2xZXC4Y2GnTOWB3khI9vXkEyfRnB_G14aw6dpldnPfgRgJa4VdHZnHUelIEqdz95dku-8hMUAisTZWSvFmpCR0plxEFZp6mr4HgqTu5MxIMmRkMGE-sdurD5mLlzYIC4TWCgxBS-zpNIjLfrBjVcyqyYBaJnc2hG1PI4wY6NLLjfhdLJkQ5UUr9gVUXAofrdxL0osxJz7ZHZcHwymMOaSA_OUA0f1R2i1DOLfI3Km-eHfPQMnw7ZmOO5_YRpRxu35lT5RMMKhmMoZq-8lI8ctfywQm5jl1NwmOfOeAeGXB0H8kg8ENO-YZtxOyYEwoJ5SaCc9Y0vQ5wmc3pd_0V)

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
