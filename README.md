<h1 align="center">DDDplus</h1>

<div align="center">

A lightweight DDD(Domain Driven Design) Enhancement Framework for complex business architecture.

[![Mavenn Central](https://img.shields.io/maven-central/v/io.github.dddplus/dddplus.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.dddplus)
![Requirement](https://img.shields.io/badge/JDK-8+-blue.svg)
[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Maintainability](https://api.codeclimate.com/v1/badges/84b05607593179e62374/maintainability)](https://codeclimate.com/github/funkygao/cp-ddd-framework/maintainability)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)
[![Mentioned in Awesome DDD](https://awesome.re/mentioned-badge.svg)](https://github.com/heynickc/awesome-ddd#jvm)
[![Gitter chat](https://img.shields.io/badge/gitter-join%20chat%20%E2%86%92-brightgreen.svg)](https://gitter.im/cp-ddd-framework/community)

</div>

<div align="center">

Languages： English | [中文](README.zh-cn.md)
</div>

----

## What is DDDplus?

DDDplus, originally cp-ddd-framework(cp means Central Platform：中台), is a lightweight DDD(Domain Driven Design) Enhancement Framework for complex business architecture.

Critical enhancements for DDD include:
1. provide [extension point](/dddplus-spec/src/main/java/io/github/dddplus/ext) with multiple routing mechanism, suited for complex business scenarios
2. [patched](/dddplus-spec/src/main/java/io/github/dddplus/model) DDD building blocks and gived clear and concise javadoc for each building block, clearing obstacles of DDD implementation
3. invent a [DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl) for reverse engineering of domain model(JavaAST), visualize complete domain knowledge from code

## Current status

Used for several complex critical central platform projects in production environment.

Latest `Maven Central` version is: `1.1.2`, under active development version is: `2.0.0-SNAPSHOT`.

## Quickstart

### Dependencies

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-runtime</artifactId>
</dependency>
```

### Integration with SpringBoot

```java
@SpringBootApplication(scanBasePackages = {"${your base packages}", "io.github.dddplus"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
```

### Reverse Modeling

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-visualization</artifactId>
</dependency>
```

Annotate your code With [DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl), DDDplus can render domain model in `PlantUML`.

```java
class ReverseModelingTest {
    @Test
    void reverseModeling() {
        DomainModelAnalyzer domainModelAnalyzer = new DomainModelAnalyzer();
        ReverseEngineeringModel domainModel = domainModelAnalyzer.scan("{your module root}")
            .analyze();
        new PlantUmlBuilder()
            .build(domainModel)
            .renderSvg("myModel.svg");
    }
}
```

### Architecture Guard

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-enforce</artifactId>
    <scope>test</scope>
</dependency>
```

Enable it by writing unit test and integrate it with CI flow.

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

## Key abstractions

![](http://www.plantuml.com/plantuml/svg/VLJ1JXj13BtxAonwIKGJH7khLX4geH8z8CGFL6RNoOxOp4GURrC4-VTwo6IpoG8vnNvlxFSydhsAIgBjge7uvFoQX5POawysubJPuuAQo3qirbI5ZVFBejWuhV_iujaCLLg6XXUA6b3SibQid72fBdY0DPLFj6HSD-tIUIoANrQCxTWBeFsSLvO5bOotjnLxTVhym34qVrbEyNbOaVCt_vHzjDAdyBqreCU61_dGkFBvBKlU1wMa2-z9rBCCqweiVf1-jyP1oXR0iendTL0KRW9LISePKiIxIyZUfzCKGASKYzV9PE1hW0_c0XqNVs0PAXvbsHVPrSLExnYWf_OXjCQnr6DKeLBn9qNEoSDVg_Xb4UI6ohhhCXgV4fn4_H1-sNVOudd52sgR8-vyFa-ac6ILHcdtHz_7TbOC6yp1c2lIiXvro1Y6hDqGyu0-XFCsGDuMAttEUytNQS9MEXkSJlkJo_nKfLkr_ZWAoviho5WNmtNmIiwp71bEcvEkt_dV9ADqjr_HL8xx_CbabbyJG1QUzm2opM6u5XV4R1-znpXuZTqzNLgNrzaXFaQ_VOf-_nIzEqMt05Vig_GX-Wy0)

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [Issue Tracker](https://github.com/funkygao/cp-ddd-framework/issues).

For any question, you can use [Gitter Chat](https://gitter.im/cp-ddd-framework/community) to ask.

## Licensing

DDDplus is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
