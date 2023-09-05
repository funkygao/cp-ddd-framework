<h1 align="center">DDDplus</h1>

<div align="center">

A lightweight DDD(Domain Driven Design) enhancement framework for forward/reverse business modeling, supporting complex system architecture evolution!

[![CI](https://github.com/funkygao/cp-ddd-framework/workflows/CI/badge.svg?branch=master)](https://github.com/funkygao/cp-ddd-framework/actions?query=branch%3Amaster+workflow%3ACI)
[![Javadoc](https://img.shields.io/badge/javadoc-Reference-blue.svg)](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.dddplus/dddplus.svg?label=Maven%20Central)](https://central.sonatype.com/namespace/io.github.dddplus)
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

DDDplus, formerly named cp-ddd-framework(cp means Central Platform：中台), is a lightweight DDD(Domain Driven Design) enhancement framework for forward/reverse business modeling, supporting complex system architecture evolution!

>It captures DDD missing concepts and patches the building block. It empowers building domain model with forward and reverse modeling. It visualizes the complete domain knowledge from code. It connects frontline developers with (architect, product manager, business stakeholder, management team). It makes (analysis, design, design review, implementation, code review, test) a positive feedback closed-loop. It strengthens building extension oriented flexible software solution. It eliminates frequently encountered misunderstanding of DDD via thorough javadoc for each building block with detailed example.

In short, the 3 most essential `plus` are:
1. [patch](/dddplus-spec/src/main/java/io/github/dddplus/model) DDD building blocks for pragmatic forward modeling, clearing obstacles of DDD implementation
2. offer a reverse modeling [DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl), visualizing complete domain knowledge from code
3. provide [extension point](/dddplus-spec/src/main/java/io/github/dddplus/ext) with multiple routing mechanism, suited for complex business scenarios

## Current status

Used for several complex critical central platform projects in production environment.

## Showcase

[A full demo of DDDplus forward/reverse modeling ->](dddplus-test/src/test/java/ddd/plus/showcase/README.md)

## Quickstart

### Forward modeling

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-runtime</artifactId>
</dependency>
```

#### Integration with SpringBoot

```java
@SpringBootApplication(scanBasePackages = {"${your base packages}", "io.github.dddplus"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
```

### Reverse Modeling

Please check out the [《step by step guide》](doc/ReverseModelingGuide.md).

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-spec</artifactId>
</dependency>
```

Annotate your code With [DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl), DDDplus will parse AST and render domain model in multiple views.

```bash
mvn io.github.dddplus:dddplus-maven-plugin:model \
    -DrootDir=${colon separated source code dirs} \
    -DplantUml=${target business model in svg format} \
    -DtextModel=${target business model in txt format}
```

### Architecture Guard

```bash
mvn io.github.dddplus:dddplus-maven-plugin:enforce \
    -DrootPackage={your pkg} \
    -DrootDir={your src dir}
```

## Known Issues

- reverse modeling assumes unique class names within a code repo

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [Issue Tracker](https://github.com/funkygao/cp-ddd-framework/issues).

For any question, you can use [Gitter Chat](https://gitter.im/cp-ddd-framework/community) to ask.

## Licensing

DDDplus is licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).
