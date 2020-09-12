package org.cdf.ddd.runtime.registry.mock.ability;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.model.BaseDomainAbility;
import org.cdf.ddd.runtime.registry.mock.domain.FooDomain;
import org.cdf.ddd.runtime.registry.mock.ext.IBazExt;
import org.cdf.ddd.runtime.registry.mock.extension.DefaultBazExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

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
