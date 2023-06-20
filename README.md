<p align="center">
<img src="doc/assets/img/logo-small.png">
</p>

[![Mavenn Central](https://img.shields.io/maven-central/v/io.github.dddplus/dddplus.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.dddplus)
![Requirement](https://img.shields.io/badge/JDK-8+-blue.svg)
[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Maintainability](https://api.codeclimate.com/v1/badges/84b05607593179e62374/maintainability)](https://codeclimate.com/github/funkygao/cp-ddd-framework/maintainability)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)

[![Mentioned in Awesome DDD](https://awesome.re/mentioned-badge.svg)](https://github.com/heynickc/awesome-ddd#jvm)
[![Javadoc](https://img.shields.io/badge/javadoc-Reference-blue.svg)](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/)
[![TODO](https://badgen.net/https/api.tickgit.com/badgen/github.com/funkygao/cp-ddd-framework?label=todos)](https://www.tickgit.com/browse?repo=github.com/funkygao/cp-ddd-framework)
[![Gitter chat](https://img.shields.io/badge/gitter-join%20chat%20%E2%86%92-brightgreen.svg)](https://gitter.im/cp-ddd-framework/community)

----

## What is DDDplus?

DDDplus, originally cp-ddd-framework(cp means Central Platform：中台), is a lightweight flexible development framework for complex business architecture.

支撑业务中台架构的DDD增强框架，是对DDD的补充和增强，核心内容包括：
- 扩展点机制
- 扩充了DDD的building blocks
- 提供了基于代码进行人工DSL标注的逆向建模机制

具体内容：
- 确定性问题
   - [IBag](/dddplus-spec/src/main/java/io/github/dddplus/model/IBag.java)，封装集合逻辑
   - ([IRule](/dddplus-spec/src/main/java/io/github/dddplus/model/IRule.java), [IBehavioralRule](/dddplus-spec/src/main/java/io/github/dddplus/model/IBehavioralRule.java)，[IDefinitionalRule](/dddplus-spec/src/main/java/io/github/dddplus/model/IDefinitionalRule.java))，业务规则对象
   - ([IUnboundedDomainModel](/dddplus-spec/src/main/java/io/github/dddplus/model/IUnboundedDomainModel.java), [BoundedDomainModel](/dddplus-spec/src/main/java/io/github/dddplus/model/BoundedDomainModel.java))，上下文角色对象
   - ([DirtyMemento](/dddplus-runtime/src/main/java/io/github/dddplus/buddy/DirtyMemento.java), [IDirtyHint](/dddplus-runtime/src/main/java/io/github/dddplus/buddy/IDirtyHint.java), [IMergeAwareDirtyHint](/dddplus-runtime/src/main/java/io/github/dddplus/buddy/IMergeAwareDirtyHint.java))，追踪领域对象状态变化，通过乐观锁实现落库
   - [Exchange](/dddplus-runtime/src/main/java/io/github/dddplus/buddy/Exchange.java)，domain与infrastructure间传递非领域数据的容器
   - [ISpecification](/dddplus-runtime/src/main/java/io/github/dddplus/buddy/specification/ISpecification.java)，业务校验和场景识别
   - [INativeFlow](/dddplus-spec/src/main/java/io/github/dddplus/model/INativeFlow.java)，可复用的流程片段
   - [AbstractBusinessNo](/dddplus-runtime/src/main/java/io/github/dddplus/buddy/vo/AbstractBusinessNo.java)，业务编号建模
   - [IUnitOfWork](/dddplus-spec/src/main/java/io/github/dddplus/model/IUnitOfWork.java)，跨聚合根的事务
- 不确定问题
   - [IDomainExtension](/dddplus-spec/src/main/java/io/github/dddplus/ext/IDomainExtension.java)，扩展点
   - [IIdentity](/dddplus-spec/src/main/java/io/github/dddplus/model/IIdentity.java)，业务身份
   - ([Policy](/dddplus-spec/src/main/java/io/github/dddplus/ext/IPolicy.java), [Router](/dddplus-runtime/src/main/java/io/github/dddplus/runtime/BaseRouter.java))，扩展点的两种路由方式
   - [Pattern](/dddplus-runtime/src/main/java/io/github/dddplus/annotation/Pattern.java)，全局业务模式
   - [Interceptor](/dddplus-runtime/src/main/java/io/github/dddplus/annotation/Interceptor.java)，扩展点拦截器
- 进程外依赖
   - [IGateway](/dddplus-spec/src/main/java/io/github/dddplus/model/IGateway.java)，远程RPC的防腐层
   - [IRepository](/dddplus-spec/src/main/java/io/github/dddplus/model/IRepository.java)，数据持久化
- 技术组件
   - [mapstruct](https://mapstruct.org/)，对象转换
- 架构守护
   - [DDDPlusEnforcer](/dddplus-enforce/src/main/java/io/github/dddplus/DDDPlusEnforcer.java)，业务建模规范的架构守护
- 可视化逆向建模
   - ([DomainModelAnalyzer](/dddplus-spec/src/main/java/io/github/dddplus/dsl/package-info.java), [PlantUmlBuilder](/dddplus-visualization/src/main/java/io/github/dddplus/ast/view/PlantUmlBuilder.java)), 分析Java AST自动生成逆向业务模型

## Current status

Used for several complex critical central platform projects in production environment.

## Quickstart

### Dependencies

#### Maven

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-runtime</artifactId>
</dependency>
```

#### Gradle

```groovy
dependencies {
    ...
    compile 'io.github.dddplus:dddplus-runtime:1.1.2'
}
```

#### Building from Source

``` bash
git clone https://github.com/funkygao/cp-ddd-framework.git
cd cp-ddd-framework/
mvn install
```

#### With dddplus-archetype

``` bash
mvn archetype:generate                          \
    -DarchetypeGroupId=io.github.dddplus        \
    -DarchetypeArtifactId=dddplus-archetype     \
    -DarchetypeVersion=1.1.1                    \
    -DgroupId=com.foo -DartifactId=demo         \
    -Dpackage=com.foo -Dversion=1.0.0-SNAPSHOT  \
    -B
```

For more, please visit [dddplus-archetype project](https://github.com/dddplus/dddplus-archetype).

### Integration with SpringBoot

```java
@SpringBootApplication(scanBasePackages = {"${your base packages}", "io.github.dddplus"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
```

### 扩展点路由原理

`Pattern`/`Partner`/`Policy`，都是`Extension#code`的提供方(它们有的通过`match(IIdentity)`方法，有的通过`extensionCode(IIdentity)`方法)，即准入规则，本质上都是把动态的业务场景转换为静态的`Extension#code`，而`Extension#code`被扩展点实例通过注解绑定，从而实现了扩展点的动态路由。

之所以设计成这样的间接路由，是基于`平台强管控`原则。

### 逆向建模

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-visualization</artifactId>
    <scope>test</scope>
</dependency>
```

在单测代码里自动生成PlantUML图：
```java
class DomainModelAnalyzerTest {
    @Test
    void reverseModeling() {
        DomainModelAnalyzer domainModelAnalyzer = new DomainModelAnalyzer();
        ReverseEngineeringModel domainModel = domainModelAnalyzer.scan({your module root})
            .analyze();
        new PlantUmlBuilder()
            .build(domainModel)
            .renderSvg("myModel.svg");
    }
}
```

### 架构守护

为了避免错误使用造成的线上事故，建议CI流水线里增加DDDplus的错误使用卡控。

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-enforce</artifactId>
    <scope>test</scope>
</dependency>
```

通过单测执行DDDplus enforcement：
```java
public class DDDPlusEnforcerTest {
    @Test
    public void enforce() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("${your base package}")
                .enforce();
    }
}
```

## Demos

- [使用DDDplus搭建`订单履约中台`的例子](https://github.com/dddplus/dddplus-demo)
- [使用DDDplus，5分钟搭建一个仓储中台WMS](https://github.com/dddplus/dddplus-archetype-demo)

### Key abstractions

![](http://www.plantuml.com/plantuml/svg/VLJ1JXj13BtxAonwIKGJH7khLX4geH8z8CGFL6RNoOxOp4GURrC4-VTwo6IpoG8vnNvlxFSydhsAIgBjge7uvFoQX5POawysubJPuuAQo3qirbI5ZVFBejWuhV_iujaCLLg6XXUA6b3SibQid72fBdY0DPLFj6HSD-tIUIoANrQCxTWBeFsSLvO5bOotjnLxTVhym34qVrbEyNbOaVCt_vHzjDAdyBqreCU61_dGkFBvBKlU1wMa2-z9rBCCqweiVf1-jyP1oXR0iendTL0KRW9LISePKiIxIyZUfzCKGASKYzV9PE1hW0_c0XqNVs0PAXvbsHVPrSLExnYWf_OXjCQnr6DKeLBn9qNEoSDVg_Xb4UI6ohhhCXgV4fn4_H1-sNVOudd52sgR8-vyFa-ac6ILHcdtHz_7TbOC6yp1c2lIiXvro1Y6hDqGyu0-XFCsGDuMAttEUytNQS9MEXkSJlkJo_nKfLkr_ZWAoviho5WNmtNmIiwp71bEcvEkt_dV9ADqjr_HL8xx_CbabbyJG1QUzm2opM6u5XV4R1-znpXuZTqzNLgNrzaXFaQ_VOf-_nIzEqMt05Vig_GX-Wy0)

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [Issue Tracker](https://github.com/funkygao/cp-ddd-framework/issues).

For any question, you can use [Gitter Chat](https://gitter.im/cp-ddd-framework/community) to ask.

## Licensing

DDDplus is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
