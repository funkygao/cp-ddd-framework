package org.ddd.cp.ddd.runtime.registry.mock.ability;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.DefaultBazExt;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IBazExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE)
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
