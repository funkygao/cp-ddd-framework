# cp-ddd-example

演示如何使用`cp-ddd-framework`框架实现一套`订单中台`。

## 目录

- [项目背景](#项目背景)
   - [解决的问题](#解决的问题)
   - [已达成目标](#已达成目标)
- [代码结构](#代码结构)
   - [依赖关系](#依赖关系)
   - 一个[订单中台代码库](#order-center-cp)
   - [中台的个性化业务包](#order-center-pattern)
   - 两个业务前台代码库
      - [KA业务前台](#order-center-bp-ka)
      - [ISV业务前台](#order-center-bp-isv)

## 项目背景

### 解决的问题

- 前台、中台如何协同开发
- 前台、中台速率天然的不匹配，如何解决

### 已达成目标

## 代码结构

### 依赖关系

![](/doc/assets/img/ddd-depgraph.png)

### [order-center-cp](order-center-cp)

订单中心的中台，通过[spec jar](order-center-cp/cp-oc-spec)为业务前台赋能，输出中台标准，并提供扩展机制。

#### [order-center-pattern](order-center-pattern)

订单中台本身的个性化业务，即个性化的业务模式包。

### 订单中心的多个业务前台

#### [order-center-bp-ka](order-center-bp-ka)

订单中心的业务前台：KA(Key Account)，关键客户的个性化业务通过扩展点的实现完成。

#### [order-center-bp-isv](order-center-bp-isv)

订单中心的业务前台：ISV，独立软件开发商的个性化业务通过扩展点的实现完成。

