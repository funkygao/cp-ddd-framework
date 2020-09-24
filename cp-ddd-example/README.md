# Demo of cp-ddd-framework

演示如何使用`cp-ddd-framework`框架实现一套`订单履约中台`。

## 目录

- [项目基本介绍](https://github.com/funkygao/cp-ddd-framework/wiki/Demo-explained-%E7%A4%BA%E4%BE%8B%E8%AF%B4%E6%98%8E)
- [代码快速入门](#代码快速入门)
- [代码结构](#代码结构)
   - [依赖关系](#依赖关系)
   - 一个[订单履约中台代码库](#order-center-cp)
   - [中台的个性化业务包](#order-center-pattern)
   - [两个业务前台代码库](#订单履约中台的多个业务前台)
      - [KA业务前台](#order-center-bp-ka)
      - [ISV业务前台](#order-center-bp-isv)

## 代码快速入门

- [中台架构特色的DDD分层架构](order-center-cp)
- 扩展点：订单的防并发
   - [如何识别该业务属于KA业务前台](order-center-cp/cp-oc-spec/src/main/java/org/example/cp/oms/spec/partner/KaPartner.java)
   - [扩展点声明](order-center-cp/cp-oc-spec/src/main/java/org/example/cp/oms/spec/ext/ISerializableIsolationExt.java)
   - [KA业务前台的实现](order-center-bp-ka/src/main/java/org/example/bp/oms/ka/extension/SerializableIsolationExt.java)
   - [ISV业务前台的实现](order-center-bp-isv/src/main/java/org/example/bp/oms/isv/extension/SerializableIsolationExt.java)
   - [扩展点被封装到DomainAbility](order-center-cp/cp-oc-domain/src/main/java/org/example/cp/oms/domain/ability/SerializableIsolationAbility.java)
   - [扩展点被调用](order-center-cp/cp-oc-domain/src/main/java/org/example/cp/oms/domain/service/SubmitOrder.java)
- [前台对中台的步骤编排](order-center-bp-ka/src/main/java/org/example/bp/oms/ka/extension/DecideStepsExt.java)
- [动态的步骤编排](order-center-cp/cp-oc-domain/src/main/java/org/example/cp/oms/domain/step/submitorder/BasicStep.java)
- [动态打包部署](order-center-cp/cp-oc-main/pom.xml)

## 代码结构

### 依赖关系

![](/doc/assets/img/ddd-depgraph.png)

### [order-center-cp](order-center-cp)

订单履约中台，通过[spec jar](order-center-cp/cp-oc-spec)为业务前台赋能，输出中台标准，并提供扩展机制。

#### [order-center-pattern](order-center-pattern)

订单履约中台本身的个性化业务，即个性化的业务模式包。

### 订单履约中台的多个业务前台

#### [order-center-bp-ka](order-center-bp-ka)

KA，关键客户的个性化业务通过扩展点的实现完成。

#### [order-center-bp-isv](order-center-bp-isv)

ISV，独立软件开发商的个性化业务通过扩展点的实现完成。
