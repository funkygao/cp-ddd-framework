# Plugin Design

## 目标

以jar包为单位管理扩展业务，扩展业务包，此处称为Plugin Jar。

Plugin有2种：
- `Pattern` + `Extension`
- `Partner` + `Extension`

一个Spring application context，每个Plugin Jar有独立的class loader隔离、热部署，热更新。

Plugin Jar可以通过maven pom静态加载，也可以事后动态加载(热更新)，支持多次热更新。

### JAVA类加载机制

- 全盘负责：一个ClassLoader装载一个类时，除非显示地使用另一个ClassLoader，该类所依赖及引用的类也由这个ClassLoader载入
- 双亲委派：子类加载器如果没有加载过该目标类，就先委托父类加载器加载该目标类，只有在父类加载器找不到字节码文件的情况下才从自己的类路径中查找并装载目标类
   - 避免重复加载
   - 安全


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

## Roadmap

### Iteration 1

- Plugin jar先不支持FatJar：所有的依赖中台提供，plugin的pom通过scope=provided来引用
   - 像spring boot那样统一管理依赖第三方包的版本
- 1个 Spring 容器，N个 PluginClassLoader

### P0

- AOP不生效
- log `JDKClassLoader loaded java.lang.annotation.Target`出现多次
- resolve of PluginClassLoader
- 第二次加载isv jar，org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.example.bp.oms.isv.IsvPartner' available
- 静态加载后，再热替换
- Spring的懒加载
- 自己带properties file
