package io.github.dddplus.runtime.registry.mock.ability;

import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.ext.ISleepExt;
import io.github.dddplus.runtime.registry.mock.extension.DefaultSleepExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE, name = "sleep")
public class SleepAbility extends BaseDomainAbility<FooModel, ISleepExt> {
    
    @Resource
    private DefaultSleepExt defaultSleepExt;

    public void sleepTooLong(FooModel model) {
        // will always throw ExtTimeoutException
        firstExtension(model, 100).sleep(1);
    }


    @Override
    public ISleepExt defaultExtension(@NotNull FooModel model) {
        return defaultSleepExt;
    }
}
