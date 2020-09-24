# Plugin Design

## Roadmap

### Iteration 1

- Plugin jar先不支持FatJar：所有的依赖中台提供，plugin的pom通过scope=provided来引用
   - 像spring boot那样统一管理依赖第三方包的版本
- 1个 Spring 容器，N个 PluginClassLoader

## 目标

以jar包为单位管理扩展业务，扩展业务包，此处称为Plugin，Plugin jar可以引用外部jar包。

Plugin有2种：
- `Pattern` + `Extension`
- `Partner` + `Extension`

一个Spring application context，每个Plugin jar有独立的class loader隔离、热部署，热更新。

## 现有解决方案

| 方案               | pros                                               | cons                                                         |
| ------------------ | -------------------------------------------------- | ------------------------------------------------------------ |
| Java 9 的模块化    | requires/exports/transitive                        |                                                              |
| OSGi               | 热替换是通过创建新对象实例，然后替换引用的方式实现 | 重，复杂，基于Bundle开发，难以驾驭；要解决有状态的问题；OSGi和非OSGi混用非常麻烦；间接依赖很难把控 |
| Pandora(OSGi)      |                                                    |                                                              |
| SOFAArk(OSGi)      |                                                    |                                                              |
| 自己做ClassLoader  |                                                    |                                                              |
| Java Agent(Arthas) |                                                    | 只能修改方法体，不能增加类：redefine                         |
| 脚本语言           |                                                    |                                                              |

### Pandora

- 最初，HSF 1.X为了解决与应用的jar冲突问题，使用OSGi来做隔离
   - 当时淘系大部分的应用都运行在JBoss中，.sar 作为JBoss支持的一种部署格式
- HSF 2.X起，隔离的功能被独立地交付给Pandora
   - 这时候的“隔离”不再是“HSF与应用的隔离”，而是“中间件与应用的隔离”以及“中间件之间的隔离”
      - 中间件包括：HSF、Notify、MetaQ、Diamond、Tair等
      - 这些中间件被称为Pandora Plugin Module，每个中间件有独立的 ModuleClassLoader
   - Pandora容器废弃了OSGi框架，只引入了它的隔离机制，重新实现ClassLoader
- PandoraBoot则将SpringBoot和Pandora进行了整合
   - convention over configuration

### SOFAArk

## 风险

- java.lang.ClassNotFoundException, NoClassDefFoundError, NoSuchMethodError, ClassFormatError, ClassCastException
- canary release, rollback, rolling upgrade, jar deployment platform

## Scenarios

- 可以自己 AOP 吗
- 打印日志
- 引入guava等第三方包，都必须是provided吗
- fat jar, lazy load
   - 不同Plugin会引入很多重复的包，导致最终的jar很大
   - Container提供provide，Plugin通过scope=provided引用
- 自己带properties file

## Knowledge

### Class Loader

**Loading Java classes during runtime dynamically to the JVM**.
