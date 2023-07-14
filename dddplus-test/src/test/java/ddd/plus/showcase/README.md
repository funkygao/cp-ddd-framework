# DDDplus Demo

## 一、Build a Warehouse Management System(WMS) with DDDplus

>Purpose: show forward and reverse modeling

### 1.1 What is WMS

A warehouse management system (WMS) consists of software and processes that allow organizations to control and administer warehouse operations from the time goods or materials enter a warehouse until they move out.

### 1.2 Reverse modeling

![](/doc/wms.svg)

[The reverse modeling source code ->](reverse/WmsReverseModelingTest.java)

### 1.3 Forward modeling

- [Application Layer](wms/application/)
   - entry point: [outbound checking](wms/application/service/CheckingAppService.java)
- [Domain Layer](wms/domain/)
- [Infrastructure Layer](wms/infrastructure/)

### 1.4 Tech details

![](/doc/tech.svg)

