<h1 align="center">DDDplus</h1>

<div align="center">

A lightweight DDD(Domain Driven Design) Enhancement Framework for complex business architecture.

[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Javadoc](https://img.shields.io/badge/javadoc-Reference-blue.svg)](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/)
[![Mavenn Central](https://img.shields.io/maven-central/v/io.github.dddplus/dddplus.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.dddplus)
![Requirement](https://img.shields.io/badge/JDK-8+-blue.svg)
[![Coverage Status](https://img.shields.io/codecov/c/github/funkygao/cp-ddd-framework.svg)](https://codecov.io/gh/funkygao/cp-ddd-framework)
[![Mentioned in Awesome DDD](https://awesome.re/mentioned-badge.svg)](https://github.com/heynickc/awesome-ddd#jvm)
[![Gitter chat](https://img.shields.io/badge/gitter-join%20chat%20%E2%86%92-brightgreen.svg)](https://gitter.im/cp-ddd-framework/community)

</div>

<div align="center">

Languages： English | [中文](README.zh-cn.md)
</div>

----

## What is DDDplus?

DDDplus, originally cp-ddd-framework(cp means Central Platform：中台), is a lightweight DDD Enhancement Framework for complex business architecture. 

It captures DDD missing concepts and patches the building block. It empowers building domain model with forward and reverse modeling. It visualizes the complete domain knowledge. It connects frontline developers with (architect, product manager, business stakeholder, management team). It makes (analysis, design, design review, implementation, code review, test) a positive feedback closed-loop. It strengthens building extension oriented flexible software solution. It eliminates frequently encountered misunderstanding of DDD via thorough javadoc for each building block.

In short, the 3 most essential `plus` are:
1. provide [extension point](/dddplus-spec/src/main/java/io/github/dddplus/ext) with multiple routing mechanism, suited for complex business scenarios
2. [patch](/dddplus-spec/src/main/java/io/github/dddplus/model) DDD building blocks for pragmatic forward modeling, clearing obstacles of DDD implementation
3. offer a reverse modeling [DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl), visualizing complete domain knowledge from code

## Current status

Used for several complex critical central platform projects in production environment.

Latest Maven Central version: `1.1.2`, under active development version: `2.0.0-SNAPSHOT`.

## Quickstart

### Showcase

[A full demo of DDDplus forward/reverse modeling ->](dddplus-test/src/test/java/ddd/plus/showcase/README.md).

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
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
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
    ReverseEngineeringModel model;
    @Test
    void visualizeDomainModel() {
        model = new DomainModelAnalyzer()
                        .scan("{your module root}")
                        .analyze();
        new PlantUmlBuilder()
            .build(model)
            .renderSvg("model.svg"); // read-only searchable graph
        new PlainTextBuilder()
            .build(domainModel)
            .render("model.txt"); // mutable, integrated with forward modeling design process
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
class ArchitectureGuardTest {
    @Test
    void enforcement() {
        new DDDPlusEnforcer()
                .scanPackages("${your base package}")
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
