## Release Notes - DDDplus - Version 1.0.3 (WIP)

* FIXED
   * Plugin reloading, Spring unable to get the Partner bean. see #20

* Improvement
   * Add `@Specification` for `ISpecification` interface: specifications are Spring beans
   * renamed CoreAopUtils -> InternalAopUtils, which is internally visible

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
