package org.ddd.cp.ddd.runtime.registry.mock.ability;

import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IReviseStepsExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = FooDomain.CODE)
public class ReviseStepsAbility extends BaseDomainAbility<FooModel, IReviseStepsExt> {

    public List<String> revisedSteps(@NotNull FooModel model) {
        return firstExtension(model).reviseSteps(model);
    }

    @Override
    public IReviseStepsExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
