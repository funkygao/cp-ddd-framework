# DSL标注代码教程

>软件架构的终极⽬标，是⽤最⼩的⼈⼒成本来满⾜构建和维护该系统的需求 ─ 《架构整洁之道》
>
>没有领域模型设计的软件，工程师往往会过多的关注到技术问题上，而忽视了产品设计和业务的目标。
>
>领域建模对于商业软件来说是非常重要的一环，也是工程师消化行业领域知识的重要方法。

```
Annotation your code with DSL --> DDDplus maven plugin --> Your visualized business model
            |                             |                           |
        meta model                   parse AST                  domain knowledge
```

## 一、用dddplus-maven-plugin跑一遍代码

```bash
mvn io.github.dddplus:dddplus-maven-plugin:model \
    -DrootDir=${冒号分隔的源代码目录名称} \
    -DplantUml=${业务模型输出到哪一个文件, svg格式} \
    -DtextModel=${业务模型输出到哪一个文件, txt格式}
```

例如：
```bash
mvn io.github.dddplus:dddplus-maven-plugin:model \
    -DrootDir=application:domain:web \
    -DrawClassSimilarity=true \
    -DsimilarityThreshold=88 \
    -DplantUml=doc/myapp.svg \
    -DtextModel=doc/myapp.txt
```

执行后，就可以在doc目录下看到一些自动生成的报告。例如，在`doc/myapp.txt`文件，会看到：
- DSL标注覆盖率
- 代码规模：多少个类，多少个方法，多少个属性，多少条语句
- 规模最大的方法 top 10
- 相似度超过`88%`的相似类

但此时由于还未标注，生成的业务模型`doc/myapp.svg`只有基础的汇总信息，看不到模型，call graph也是空的。

## 二、开始DSL标注

### 2.1 引入pom依赖

在需要标注的代码模块都需要引入`dddplus-spec`依赖：可以放心使用，它不会引入任何间接依赖。

所有的DSL注解的有效范围都是`RetentionPolicy.SOURCE`，不会污染被标注代码。

```xml
<dependency>
    <groupId>io.github.dddplus</groupId>
    <artifactId>dddplus-spec</artifactId>
</dependency>
```

### 2.2 开始DSL标注

请参考[DSL参考手册](https://funkygao.github.io/cp-ddd-framework/doc/apidocs/io/github/dddplus/dsl/package-summary.html)。

#### 2.2.1 从 `@Aggregate` 开始标注

逆向建模自动生成的业务模型，是以`Aggregate`为边界进行组织的。

```
Aggregate
  ├── KeyEvent
  ├── KeyUsecase
  └── KeyModel
      ├── KeyBehavior
      ├── KeyElement
      ├── KeyFlow
      ├── KeyRelation
      ├── KeyRule
      └── KeyUsecase
```

`Aggregate`与`DDD`里的聚合概念一致，是业务边界；非DDD项目可以理解为模块。

具体地，在某个`package`下创建`package-info.java`，例如：

```java
@Aggregate(name = "复核报差异")
package ddd.plus.showcase.wms.domain.diff;
```

#### 2.2.2 标注核心实体类

请参考我们提供的[Carton示例](../dddplus-test/src/test/java/ddd/plus/showcase/wms/domain/carton/Carton.java)。

这涉及到的DSL包括：
- KeyElement 关键业务字段
- KeyRelation 业务实体间重要关系
- KeyBehavior 该实体的关键业务行为
- KeyRule 该实体的关键业务规则

#### 2.2.3 标注服务与流程片段类

贫血模型往往把业务逻辑写到各个`Service`/`Worker`/`Utils`/`Validator`/`Processor`等类里，这不符合面向对象思想。

通过`@KeyFlow`可以把行为职责重新分配到业务实体上，提升模型可理解性，例如：

```java
public class FooService {

    @KeyFlow(actor = Order.class) // actor是行为主体的意思
    public void doSth(Order order) {
    }
}
```

有时候，目前代码里并不存在一个恰当的业务概念抽象，在逆向过程中发现了它，但重构工作量太大不敢做，这时候可以先让这个业务概念仅存在于逆向模型里。

具体办法是为该业务概念创建一个类，实现`IVirtualModel`接口，相关的行为职责通过`KeyFlow#actor`赋予到这个类上。

#### 2.2.4 标注业务入口

业务入口，通常位于`RPC接口`/`MQ Consumer`/`Controller`等类，在这些类的关键方法上标注`@KeyUsecase`，例如：

```java
public class CheckingController {
    // 提交复核任务
    @KeyUsecase
    public ApiResponse<String> submitTask(@Valid SubmitTaskDto dto) { }
}
```

#### 2.2.5 标注业务事件

通过`@KeyEvent`标注在业务事件类上，例如：

```java
@KeyEvent
public class OrderShippedEvent {
}
```

#### 2.2.6 标注建议

>Since the recovered diagrams are intended to be inspected by a human, the presentation modes should take into account the cognitive limitations of humans explicitly. 

- 标注过程，不要一次性做全，可以做一点看一点，试探性摸索，熟悉后再做彻底标注
- 图形有效的前提是不能有太多元素，不要追求大而全，请主动忽略非关键细节，人为降噪，以避免范围失控丢失焦点
   - 好的模型能尽可能简单的情况下较好的拟合事物

## 三、DSL标注后执行dddplus-maven-plugin

在标注过程中，可以随时执行`dddplus-maven-plugin`，以观察逆向业务模型的变化。

## 四、最佳实践

为了与日常开发、设计评审、代码评审等结合起来，建议把逆向生成的模型文件纳入版本控制，从而完整追溯业务模型的演进过程。

## 五、模型思维

>用一个较为简单的东西来代表另一个东西，这个简单的东西被叫做模型。─ 维基百科
>
>业务设计上往往没有建立起特定的领域模型，这是我们架构腐化和软件开发困难的关键原因。业务领域建立好的模型，并指导代码实践，这就是 “编程思维”。

通过模型思维来看待软件开发，我们会发现，软件从设计到开发的过程就是各种模型的转换。
![](/doc/assets/img/model-of-ee.png)

模型分为形式化和非形式化两种。形式化的模型是精确描述的模型，而非形式化的模型是一些非精确描述的模型，主要用来做商业、业务探索。

对于应用开发的软件工程师来说，核心的问题并非如何编写代码，而是如何将非形式化的业务输入(模型)进行合理抽象、设计，并转换为形式化的过程。

>某种程度上来说，通过高级语言编写的代码也是一种模型。在多年以前，计算机科学家们认为编写 Java 代码 的人不算程序员，可以由业务人员直接编写业务软件。由于软件工程中`非形式化`和`形式化`之间存在巨大鸿沟，编程就是模型的形式化过程，从这个角度看能深刻分析业务并获得良好抽象结果的程序员具有竞争力，并不会被 AI 编程所代替。

