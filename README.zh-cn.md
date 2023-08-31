<h1 align="center">DDDplus</h1>

<div align="center">

轻量级DDD正向/逆向业务建模框架，支撑复杂业务系统的架构演化！

[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Javadoc](https://img.shields.io/badge/javadoc-Reference-blue.svg)](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.dddplus/dddplus.svg?label=Maven%20Central)](https://central.sonatype.com/namespace/io.github.dddplus)
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

软件开发不仅仅是业务交付的生产过程，本质上是系统化的知识工程，大型复杂软件开发的核心难点是如何处理隐藏在业务知识中的核心复杂度，除了清晰地理解业务诉求之外，还需要通过建模的方式对这种复杂度进行简化与精炼。

作为软件交付最终产品的代码，由于具有(可运行，包含完全细节，演进过程完整追溯，自我修复)特点而成为业务的唯一事实真相，但代码如何以易于理解的形式直观反映业务知识一直是业界难题。

`DDDplus`扩充了DDD，通过正向的DDD建模，配合基于DSL声明式标注从而让代码自动生成业务模型的逆向建模过程，实现了：代码与模型统一，统一语言，抑制熵增。

>它捕获了DDD里缺失的构造块；它为正向和逆向业务建模赋能；它把代码可视化成完整的业务知识；它连接了(架构师，产品经理，业务方，管理者)；它把(业务分析，设计，设计评审，开发实现，代码评审，测试)成为一个正反馈的闭环；它方便构建面向扩展的灵活平台架构；它纠正了常见的DDD的错误理解。

简单地讲，DDDplus的`plus`最关键核心是：
- [扩充](/dddplus-spec/src/main/java/io/github/dddplus/model)了DDD的building blocks，解决DDD落地难问题
- 逆向建模的[DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl)，让代码可视化地表达完整业务模型
- 支持多种路由模式的[扩展点机制](/dddplus-spec/src/main/java/io/github/dddplus/ext)，应对复杂业务场景

## 项目现状

应用于多个大型核心复杂项目的生产环境。

## 项目演示

[正向和逆向建模的项目演示 ->](dddplus-test/src/test/java/ddd/plus/showcase/README.zh-cn.md)

## 快速入门

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

### 逆向建模

`DDDplus`里的基于DDD的正向建模，与基于AST静态分析的逆向建模是相互独立的。如果你觉得DDD落地太难，那么可以只使用逆向建模部分，即使一个遗留系统，也可以使用逆向建模功能：让代码承载领域知识，生成业务洞见，还原架构设计，识别代码设计缺陷，为需求分析提供依据。

请参考[《逆向建模教程》](doc/ReverseModelingGuide.md)。

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-visualization</artifactId>
</dependency>
```

通过[DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl)在代码进行标注后，自动生成多视角视图。

```bash
mvn io.github.dddplus:dddplus-maven-plugin:model \
    -DrootDir=${colon separated source code dirs} \
    -DplantUml=${target business model in svg format} \
    -DtextModel=${target business model in txt format}
```

### 架构守护

为了避免错误使用造成的线上事故，建议CI流水线里增加DDDplus的错误使用门禁。

```bash
mvn io.github.dddplus:dddplus-maven-plugin:enforce \
    -DrootPackage={your pkg} \
    -DrootDir={your src dir}
```

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [Issue Tracker](https://github.com/funkygao/cp-ddd-framework/issues).

For any question, you can use [Gitter Chat](https://gitter.im/cp-ddd-framework/community) to ask.

## Licensing

DDDplus is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
