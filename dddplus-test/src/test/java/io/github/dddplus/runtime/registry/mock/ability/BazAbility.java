package io.github.dddplus.runtime.registry.mock.ability;

import io.github.dddplus.runtime.registry.mock.ext.IBazExt;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.extension.DefaultBazExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE, tags = {AbilityTag.bar, AbilityTag.foo})
@Slf4j
public class BazAbility extends BaseDomainAbility<FooModel, IBazExt> {

    @Resource
    private DefaultBazExt defaultBazExt;

    public Integer guess(FooModel model) {
        return firstExtension(model).execute(model);
    }

    @Override
    public IBazExt defaultExtension(@NotNull FooModel model) {
        return defaultBazExt;
    }
}
