# 2.x vs 1.x

## Incompatible changes

- Removed
   `IBaseTranslator`, `ApiResult`, `RequestProfile`, `IModelAttachmentExt`, `IDomainModelCreator`
- Renamed
   - `IExtPolicy` -> `IPolicy`
   - `BaseDomainAbility` -> `BaseRouter`, `@DomainAbility` -> `@Router`
   - `DDD.findAbility` -> `DDD.useRouter`/`DDD.usePolicy`
   - `Reducer` -> `IReducer`
- Deprecated
   - `@Domain`, `@DomainService`, `@Step`, `StepsExecTemplate`, `IDecideStepsExt`, `IDomainStep`
- Function Signature
   - all `IDomainExtension` input argument changed from `IDomainModel` to `IIdentity`

## New features

- [Reverse Modeling DSL](/dddplus-spec/src/main/java/io/github/dddplus/dsl/package-info.java)
- Patches DDD building blocks
- [Pragmatic DDDplus Showcase Project](/dddplus-test/src/test/java/ddd/plus/showcase/)

## Bug Fix

- b90bd6a71b66f5b1c60460949bdd8b7ab833f854
