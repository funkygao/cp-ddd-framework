<h1 align="center">DDDplus Demo</h1>

<div align="center">

Build a Warehouse Management System(WMS) with DDDplus.

</div>

<div align="center">

Languages： English | [中文](README.zh-cn.md)

</div>

----

## 一、What is WMS

A warehouse management system (WMS) consists of software and processes that allow organizations to control and administer warehouse operations from the time goods or materials enter a warehouse until they move out.

## 二、How to read the demo

>3.1(learn from reverse model) -> 3.2(learn code implementation from entry point) -> 3.3(domain object call graph) -> 3.5(tech details)

## 三、Details

### 3.1 Reverse modeling

![](/doc/showcase/wms.svg)

[The reverse modeling source code ->](reverse/WmsReverseModelingTest.java)

### 3.2 Forward modeling

- [Application Layer](wms/application/)
   - entry point: [outbound checking](wms/application/service/CheckingAppService.java)
- [Domain Layer](wms/domain/)
- [Infrastructure Layer](wms/infrastructure/)

### 3.3 Call graph

![](/doc/showcase/callgraph.svg)

### 3.4 Package reference graph

![](/doc/showcase/pkgref.svg)

### 3.5 Tech details

![](/doc/showcase/tech.svg)

