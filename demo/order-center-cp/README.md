# order-center-cp(订单履约中台)

## 应用的入口

[服务器启动](cp-oc-main/src/main/java/org/example/cp/oms/OrderServer.java)

## HTTP Controller

[订单履约中台的Controller](cp-oc-controller/src/main/java/org/example/cp/oms/controller/OrderController.java)

## 分层架构

![](/doc/assets/img/ddd-depgraph.png)

### 具有中台特色的DDD分层架构

通过[spec jar](cp-oc-spec)为业务前台赋能，输出中台标准，并提供扩展机制。
