## Release Notes - DDDplus - Version 1.0.3 (WIP)

* FIXED
   * Plugin reloading, Spring unable to get the Partner bean. see https://github.com/funkygao/cp-ddd-framework/issues/20
   * ArchitectureEnforcer的接口规范 bug，需要把注解排除在外

* Improvement
   * Add `@Specification` for `ISpecification` interface: specifications are Spring beans
   * renamed CoreAopUtils -> InternalAopUtils, which is internally visible
   * 演示如何对step进行方法拦截

* Feature
   * Step可以异步执行，同步回滚
   * 提供`DomainArtifacts`，方便业务能力可视化

* Test
   * Stress test for plugin jar reloading passed

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
