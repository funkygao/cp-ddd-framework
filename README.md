# cp-ddd-framework (Central Platform：中台)

![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)
[![Code Quality: Java](https://img.shields.io/lgtm/grade/java/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/context:java)
[![Total Alerts](https://img.shields.io/lgtm/alerts/g/funkygao/cp-ddd-framework.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/funkygao/cp-ddd-framework/alerts/)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)
[![Gitter](https://img.shields.io/gitter/room/cp-ddd-framework/cp-ddd-framework.svg)](https://gitter.im/cp-ddd-framework/community)

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

![](http://www.plantuml.com/plantuml/svg/XLBBRjim4BppAtXC3lu1mp2ImZtaKA18UozMvCeYJ976ScMdQFllNLbiAveiSfDsXhEFcQV4G3oDNgMJ2sSW6FK0mLx9CMFGJ1Eke3I0ZLSNZhlTrV7mRVdVxnUUnvurHqtkEB2sZj2mYq4f5nYf1uFwvH17SE4BqiKPR02CPxNwrbkDRsWcneV7Wet6MPmPwUCRrpglU8adw3ncmReeoXIzxy2S2kdC5LKCIQPVGoyM0pl-_eef-WlI6liKBVfdPZ33AAnvgaRjji_PImDfsiO9WqKI6Bh418PrNXFDLnpbBUC_w0CaN0P-xfoNqLHL4SaZyjzzHBX5wHsbTqaMUvXyGQjcAnmx70xo_oCni3E_f4cmMQqDtjG4klDtRfqB2ruv8OmHs0mFsRxRPH-LAlEcTpytctNKvbaPWoqYbqbbP6fsgLexiTLEBeApiKcf-rZbBkMUHOEANMLi_Q8-oVBcCp0ZXV_3n059DUpU2ImMpT4e3rGYhTNQv_3H06E3lzdi_yovrPrejYbvranH-LUSDz6YoWBvjrR8zKPWHRlCH2GpO682beVg2OETHlyR)

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

## Building from Source

``` bash
mvn clean install
```

## Licensing

cp-ddd-framework is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
