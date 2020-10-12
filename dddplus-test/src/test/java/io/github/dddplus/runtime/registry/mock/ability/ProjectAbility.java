package io.github.dddplus.runtime.registry.mock.ability;

import io.github.dddplus.runtime.registry.mock.ext.IProjectExt;
import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

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
