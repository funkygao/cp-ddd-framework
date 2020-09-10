# cp-ddd-framework (Central Platform：中台)

* [What is cp-ddd-framework](#what-is-cp-ddd-framework)
   * [What problems does it solve](#what-problems-does-it-solve)
   * [Key features](#key-components)
   * [Key abstractions](#key-abstractions)
   * [Modules](#modules)
   * [Landscape](#landscape)
* [Requirements](#requirements)
* [Using cp-ddd-framework](#using-cp-ddd-framework)
* [Building from Source](#building-from-source)
* [Documentaion](#documentation)
* [Roadmap](#roadmap)
* [FAQ](#faq)
* [Licensing](#licensing)

## What is cp-ddd-framework?

cp-ddd-framework is a lightweight development framework for complex business architecture.

轻量级业务中台开发框架，以DDD思想为基础，打造业务不确定的业务中台：企业级能力复用平台。

### What problems does it solve

- 业务逻辑的个性化：uncertain biz logic
- 业务流程的个性化：uncertain biz flow
- 业务模型的个性化：uncertain biz model
- 业务前台与中台如何协同：synergy between BP and CP teams
- and more

### Key features

- layered extension point
- model schema extension
- business steps orchestration
- dynamic loading business extension jar
- isolation of business runtime
- best practice of DDD
- and more

### Key abstractions

![](http://www.plantuml.com/plantuml/png/TLBBZjim3BphAmZqidl83nX1i0_di8S2nRRtWfQOM8Wi1HITRD7stwiaB637sbiuCvhCa7xAWYpZ44m--7X4nW5wZExCNeGY28yqeJgqXpsdCRhN51B3b-rs-zA-JXIvHvVEFkxNwxEN_kTjtcnkJe5s0fA0_RuNS9x9Ya_H61-5U8UMuArD0_hudVZaBLK8eQEP_8O6w8FiAFJmE5EJzOukYhpzaANFEvtI0Rj0QZW7rPbJ29VECslL5bUAkQIVKc-EedZvzKuvXHFnbFkQ78Ld4RJzK5INL1NiskSOqCVoftMUAJhYGcENXT4AQ4soFsWeRuNC0fdjgMtRCl_EWkBjNzZvw9ux7qhmEdbrAzJTLelCAQ3yVV-Y651i_w1Bk_PvD6P-PLZ_MQq-Ex2IrQjLN5umqmtMHiRS7qmTvrtggkhLbZLlscyeGXm_fraYiJ6BYB0kT5dxhBPYjVYrcWs4P56eeb-jDl-pDT3vwEeIpQSIeoCqO9CZemOwBXqKhtnu5-z9iGOIsvwoC4fvQ9uekd48_m00)

### Modules

#### cp-ddd-spec

#### cp-ddd-runtime

#### cp-ddd-enforce

### Landscape

![](doc/assets/img/landscape.png)

## Requirements

Requires Java 1.8+ and Spring 4.3.12.RELEASE or later.

## Using cp-ddd-framework

See the [cp-ddd-example](cp-ddd-example).

## Building from Source

``` bash
mvn clean install
```

## Documentation

[cp-ddd-framework Javadoc](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/).

## Roadmap

## FAQ

- 什么是扩展点?
   - 业务语义一致，但执行逻辑不同的业务功能点
   - 在cp-ddd-framework里，就是一个java interface
- Where to find the detailed documentation?
   - We have the belief: code itself is documentation.

## Licensing

cp-ddd-framework is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
