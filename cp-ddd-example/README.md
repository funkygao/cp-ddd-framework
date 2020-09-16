# cp-ddd-example

演示如何使用`cp-ddd-framework`框架实现一套`订单中台`。

## 目录

- [项目背景](#项目背景)
   - [基本思想](#基本思想)
   - [已解决的问题](#已解决的问题)
   - [代码快速入门](#代码快速入门)
- [代码结构](#代码结构)
   - [依赖关系](#依赖关系)
   - 一个[订单中台代码库](#order-center-cp)
   - [中台的个性化业务包](#order-center-pattern)
   - 两个业务前台代码库
      - [KA业务前台](#order-center-bp-ka)
      - [ISV业务前台](#order-center-bp-isv)

## 项目背景

中台要实现一套`订单中台`(OMS)，以订单为线索解决销售订单的物流履约全周期。

该系统对接的上游，即业务前台BP有近10个，每个BP的团队规模和经营规模不同，对新功能交付速度的要求也不同。本示例仅列举两个典型的BP部门：
- KA(Key Account)，即关键客户，全行业覆盖
   - KA的话语权大，他们往往把内部生产和管理的形态传递(污染)到这套`订单中台`
   - KA的需求是无法预测的，不确定的，无法自顶向下设计；即使同一个行业的KA，需求也千差万别
   - 每个KA都有一系列的个性化业务，字段要求不同，流程不同，业务逻辑不同
      - 例如，仓储出库打印面单不同，目标出库仓算法不同，库存预占模式不同，生产状态回传节点不同等
- ISV，独立软件开发商

相对于普通的平台化，中台不仅要解决烟囱式架构问题，还要解决前台、中台组织的问题，本质上它是一个复杂生态下的协作问题。

同时，`订单中台`本身也有很多个性化的业务，也需要架构设计上支撑，避免补丁式开发。

### 基本思想

中台是业务的入口和驱动者，业务模型和数据模型沉淀在中台。

在业务执行过程中，`cp-dddframework`自动识别出业务扩展点的具体实现者，并把控制权交给相应的前台；扩展点方法执行完成后，回到中台继续执行。可以类比OS的上下文切换。

### 已解决的问题

- 系统上
   - 一套代码，满足所有场景需求
   - 一套代码，多种打包部署模式，支持国内和国际业务
   - 代码本身知识化，沉淀业务资产
   - 中台的业务代码如何分层设计，层间如何交互，如何让研发拿到需求立刻就知道代码写在哪里，不各显神通地造轮子造概念
   - 如何快速响应千奇百怪的个性化需求，同时保持自身不腐化
   - 一套模型，如何消化大量的个性化字段
   - 中台如何控制多前台业务包的风险，jar冲突，被拖死等问题
- 组织上
   - 前台、中台如何解耦，如何协同发展，如何速率匹配
   - 前台如何复用、编排中台能力，如何扩展中台能力来实现自助式发展
   - 支持前台、中台协同开发，前台可以方便地使用和扩展中台能力，独立实现市场需求

### 代码快速入门

- [中台的分层架构](order-center-cp)
   - 相对DDD，多了[spec](order-center-cp/cp-oc-spec)一层
   - 该层用于前台、中台解耦
- [中台对前台输出的能力和标准](order-center-cp/cp-oc-spec/src/main/java/org/example/cp/oms/spec)
- [如何识别该业务属于KA业务前台](order-center-cp/cp-oc-spec/src/main/java/org/example/cp/oms/spec/partner/KaPartner.java)
- 订单的防并发机制扩展点
   - [扩展点声明](order-center-cp/cp-oc-spec/src/main/java/org/example/cp/oms/spec/ext/ISerializableIsolationExt.java)
   - [KA业务前台的实现](order-center-bp-ka/src/main/java/org/example/bp/oms/ka/extension/SerializableIsolationExt.java)
   - [ISV业务前台的实现](order-center-bp-isv/src/main/java/org/example/bp/oms/isv/extension/SerializableIsolationExt.java)
- [前台对中台的步骤编排](order-center-bp-ka/src/main/java/org/example/bp/oms/ka/extension/DecideStepsExt.java)
- [动态的步骤编排](order-center-cp/cp-oc-domain/src/main/java/org/example/cp/oms/domain/step/submitorder/BasicStep.java)
- [动态打包部署](order-center-cp/cp-oc-main/pom.xml)

## 代码结构

### 依赖关系

![](/doc/assets/img/ddd-depgraph.png)

### [order-center-cp](order-center-cp)

订单中心的中台，通过[spec jar](order-center-cp/cp-oc-spec)为业务前台赋能，输出中台标准，并提供扩展机制。

#### [order-center-pattern](order-center-pattern)

订单中台本身的个性化业务，即个性化的业务模式包。

### 订单中心的多个业务前台

#### [order-center-bp-ka](order-center-bp-ka)

订单中心的业务前台：KA，关键客户的个性化业务通过扩展点的实现完成。

#### [order-center-bp-isv](order-center-bp-isv)

订单中心的业务前台：ISV，独立软件开发商的个性化业务通过扩展点的实现完成。

