package org.cdf.ddd.runtime.registry.mock.ability;

import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.model.BaseDomainAbility;
import org.cdf.ddd.runtime.registry.mock.domain.FooDomain;
import org.cdf.ddd.runtime.registry.mock.ext.IProjectExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE, name = "project")
public class ProjectAbility extends BaseDomainAbility<FooModel, IProjectExt> {

    public String submit(FooModel model) {
        return String.valueOf(firstExtension(model).execute(model));
    }

    @Override
    public IProjectExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
