<h1 align="center">DDDplus</h1>

<div align="center">

轻量级DDD增强框架！

[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Javadoc](https://img.shields.io/badge/javadoc-Reference-blue.svg)](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/)
[![Mavenn Central](https://img.shields.io/maven-central/v/io.github.dddplus/dddplus.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.dddplus)
![Requirement](https://img.shields.io/badge/JDK-8+-blue.svg)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)
[![Mentioned in Awesome DDD](https://awesome.re/mentioned-badge.svg)](https://github.com/heynickc/awesome-ddd#jvm)
[![Gitter chat](https://img.shields.io/badge/gitter-join%20chat%20%E2%86%92-brightgreen.svg)](https://gitter.im/cp-ddd-framework/community)

</div>

<div align="center">

Languages： [English](README.md) | 中文
</div>

----

## DDDplus是什么

DDDplus是轻量级DDD补充和增强的框架。

它捕获了DDD里缺失的构造块；它为正向和逆向业务建模赋能；它把代码可视化成完整的业务知识；它连接了(架构师，产品经理，业务方，管理者)；它把(业务分析，设计，设计评审，开发实现，代码评审，测试)成为一个正反馈的闭环；它方便构建面向扩展的灵活平台架构；它纠正了常见的DDD的错误理解。

简单地讲，DDDplus的`plus`最关键核心是：
- 支持多种路由模式的[扩展点机制](/dddplus-spec/src/main/java/io/github/dddplus/ext)，应对复杂业务场景
- [扩充](/dddplus-spec/src/main/java/io/github/dddplus/model)了DDD的building blocks，解决DDD落地难问题
- 逆向建模的[DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl)，让代码可视化地表达完整业务模型

## 项目现状

应用于多个大型核心复杂项目的生产环境。

`Maven Central` 最新版本: `1.1.2`, 正在积极开发的版本: `2.0.0-SNAPSHOT`。

## 快速入门

### 项目演示

[正向和逆向建模的项目演示](dddplus-test/src/test/java/ddd/plus/showcase/README.zh-cn.md)

### 正向建模

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-runtime</artifactId>
</dependency>
```

#### 与SpringBoot集成

```java
@SpringBootApplication(scanBasePackages = {"${your base packages}", "io.github.dddplus"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
```

#### 扩展点路由原理

`Pattern`/`Partner`/`Policy`，都是`Extension#code`的提供者(它们有的通过`match(IIdentity)`方法，有的通过`extensionCode(IIdentity)`方法)，即准入规则，本质上都是把动态的业务场景转换为静态的`Extension#code`，而`Extension#code`被扩展点实例通过注解绑定，从而实现了扩展点的动态路由。

之所以设计成这样的间接路由，是基于`平台强管控`原则。

### 逆向建模

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-visualization</artifactId>
</dependency>
```

通过[DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl)在代码进行标注后，即可自动PlantUML类图形式的业务模型：

```java
class ReverseModelingTest {
    @Test
    void reverseModeling() {
        DomainModelAnalyzer domainModelAnalyzer = new DomainModelAnalyzer();
        ReverseEngineeringModel domainModel = domainModelAnalyzer.scan("{your module root}")
            .analyze();
        new PlantUmlBuilder()
            .build(domainModel)
            .renderSvg("myModel.svg"); // read-only searchable graph
        new PlainTextBuilder()
            .build(domainModel)
            .render("model.txt"); // mutable, integrated with forward modeling design process
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

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [Issue Tracker](https://github.com/funkygao/cp-ddd-framework/issues).

For any question, you can use [Gitter Chat](https://gitter.im/cp-ddd-framework/community) to ask.

## Licensing

DDDplus is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
