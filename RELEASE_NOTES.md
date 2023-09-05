## Release Notes - DDDplus - 2.1.0

* Feature
   * 通过apache bcel库实现完全非侵入式call graph图
      * 提示哪些类和方法在关系图上可能被排除
      * 支持用户自定义类关系
   * 提供io.github.dddplus:dddplus-maven-plugin的三个功能
      * 与IDEA内置的Call Hierarchy功能相比
         * IDEA只能基于单个方法分析，DDDplus全局分析
         * 提供配置驱动的噪音过滤机制，使得自动生成的关系图突出重点
      * model 基于DDDplus DSL标注产生的逆向业务模型
      * call 完全非侵入式基于字节码增强机制分析jar包生成类方法调用关系图
      * polymorphism 完全非侵入式基于AST Parser自动生成具有多态的类关系图
      * enforce DDDplus正向建模的架构约束强化工具

## Release Notes - DDDplus - 2.0.3

* Feature
   * 根据源代码AST分析，形成结构化数据，导出到sqlite数据库
   * 生成原始的plantuml文件
   * 可以修改实体所在的包
   * KeyUsecase图里显示类的javadoc
   * 业务字典，枚举类，也可以使用 KeyElement 进行标注
   * PlantUML类图里，KeyFlow对应的文件可以点击，自动在IDEA中打开到对应位置

* Bug Fix
   * 逆向分析时，KeyRelation会把一个类放在了错误的package


## Release Notes - DDDplus - 2.0.2

* Feature
   * 提供详细的《逆向建模教程》
   * ClassMethodReport 增加大方法的自动发现
   * KeyElement#types 增加默认值，降低DSL标注成本
* Fix
   * CallGraphAstNodeVisitor 运行是抛出异常导致无法继续执行

## Release Notes - DDDplus - 2.0.1-RELEASE

* 新功能
   * 从代码里自动生成方法的call graph图，用于评估代码改动的影响范围和风险，从宏观上对代码结构和类之间关系有了洞察
      * 由于只关注DSL标注的方法，去除了噪音，这样的图才不会混乱
   * 从代码里自动分析包之间的交叉引用图，用于发现不合理依赖关系，包的设计是否合理
   * 新增dddplus-maven-plugin模块，把静态检查、代码可视化等功能集成到maven插件里，方便使用和集成
   * 扩展点的方法返回值不能为primitive type，以避免NPE，之前只是规范说明，目前增加了ExtensionMethodSignatureEnforcer，结合CI可以彻底杜绝此类问题

* Feature
   * add ExtensionMethodSignatureEnforcer to avoid NPE risk
   * call graph complete and exact click through
   * add dddplus-maven-plugin
   * visualize package cross reference in svg file

## Release Notes - DDDplus - 2.0.0-SNAPSHOT

* FIXED
   * [如果所有pattern扩展点叠加执行，导致 ClassCastException](https://github.com/dddplus/dddplus/commit/b90bd6a71b66f5b1c60460949bdd8b7ab833f854)

* Feature
   * [Reverse Modeling DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl/package-info.java)
   * Patches DDD with complete [building blocks](/dddplus-spec/src/main/java/io/github/dddplus/model/)
   * [Pragmatic DDDplus Showcase Project](/dddplus-test/src/test/java/ddd/plus/showcase/)

* Incompatible changes with 1.x.y
   * Removed
      * `IBaseTranslator`, `ApiResult`, `RequestProfile`, `IModelAttachmentExt`, `IDomainModelCreator`
   * Renamed
      * `IExtPolicy` -> `IPolicy`
      * `BaseDomainAbility` -> `BaseRouter`, `@DomainAbility` -> `@Router`
      * `DDD.findAbility` -> `DDD.useRouter`/`DDD.usePolicy`
      * `Reducer` -> `IReducer`
   * Function signature changed
      * all `IDomainExtension` input argument changed from `IDomainModel` to `IIdentity`

## Release Notes - DDDplus - Version 1.1.1

* FIXED
   * [#39](https://github.com/funkygao/cp-ddd-framework/issues/39) 解决Policy定位的扩展点如果不存在则抛出NullPointerException

## Release Notes - DDDplus - Version 1.1.0

* FIXED
   * 解决了`BaseDomainAbility`由于泛型机制不支持继承的问题
   * [#7](https://github.com/dddplus/dddplus/issues/7) 解决`DomainArtifacts`暴露扩展点不全的问题：如果一个扩展点只被`Partner`实现，就没有暴露
   * [#30](https://github.com/funkygao/cp-ddd-framework/issues/30) 解决了Plugin jar reload后，`DomainArtifacts`没有刷新的问题

* Feature
   * [#37](https://github.com/funkygao/cp-ddd-framework/issues/37) 除了`Partner/Pattern`的静态的扩展点路由机制外，提供了动态路由机制：`Policy`，供使用者扩展
   * [#35](https://github.com/funkygao/cp-ddd-framework/issues/35) 提供绕过`BaseDomainAbility`而直接路由扩展点的机制，在业务属性不明显的场景下使用
   * [#32](https://github.com/funkygao/cp-ddd-framework/issues/32) 框架提供默认的步骤编排能力，使用者不必从头编写`Ability`来编排步骤
   * 增加了一个使用`DDDplus`来搭建`low-code`平台的例子工程：https://github.com/dddplus/easyapp
      * 其中的Trigger机制依靠扩展点和Plugin的动态加载实现

## Release Notes - DDDplus - Version 1.0.3

* FIXED
   * [#20](https://github.com/funkygao/cp-ddd-framework/issues/20) Plugin reloading, Spring unable to get the Partner bean
   * [#28](https://github.com/funkygao/cp-ddd-framework/issues/28) ArchitectureEnforcer的接口规范 bug，需要把注解排除在外
   * [dddplus/dddplus#4](https://github.com/dddplus/dddplus/issues/4) 在Spring Boot集成时，无法触发`IStartupListener`

* Improvement
   * [#19](https://github.com/funkygao/cp-ddd-framework/issues/19) Add `@Specification` for `ISpecification` interface: specifications are Spring beans
   * [#29](https://github.com/funkygao/cp-ddd-framework/issues/29) 演示如何对step进行方法拦截

* Feature
   * [#24](https://github.com/funkygao/cp-ddd-framework/issues/24) Step可以异步执行，同步回滚
   * [#23](https://github.com/funkygao/cp-ddd-framework/issues/23) 提供`DomainArtifacts`，方便业务能力可视化

* Test
   * [#26](https://github.com/funkygao/cp-ddd-framework/issues/26) Stress test for plugin jar reloading passed

## Release Notes - DDDplus - Version 1.0.2

* FIXED
   * NPE when Plugin load without using Spring

* Improvement
   * Add Specification & Notification Pattern to DDDplus for explicit business rules expression

## Release Notes - DDDplus - Version 1.0.1

* Improvement
   * Integrated with CodeQL
   * Being renamed to DDDplus
   * Container.java and Plugin.java refactored: explicit over implicit
   * Plugin jar reloading will not allow concurrency

* Test
   * NamedThreadFactory test case bug fixed
