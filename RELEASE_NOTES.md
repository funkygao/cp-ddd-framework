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
