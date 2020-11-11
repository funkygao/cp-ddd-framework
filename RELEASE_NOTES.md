## Release Notes - DDDplus - Version 1.0.3 (WIP)

* FIXED
   * [#20](https://github.com/funkygao/cp-ddd-framework/issues/20) Plugin reloading, Spring unable to get the Partner bean
   * [#28](https://github.com/funkygao/cp-ddd-framework/issues/28) ArchitectureEnforcer的接口规范 bug，需要把注解排除在外
   * [dplus/dplus#4](https://github.com/dddplus/dddplus/issues/4) 在Spring Boot集成时，无法触发`IStartupListener`

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
