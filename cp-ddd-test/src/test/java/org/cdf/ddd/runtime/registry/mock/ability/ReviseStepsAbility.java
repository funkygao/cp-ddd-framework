package org.cdf.ddd.runtime.registry.mock.ability;

import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.model.BaseDomainAbility;
import org.cdf.ddd.runtime.registry.mock.domain.FooDomain;
import org.cdf.ddd.runtime.registry.mock.ext.IReviseStepsExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = FooDomain.CODE, value = "mockReviseStepsAbility")
public class ReviseStepsAbility extends BaseDomainAbility<FooModel, IReviseStepsExt> {

    public List<String> revisedSteps(@NotNull FooModel model) {
        return firstExtension(model).reviseSteps(model);
    }

    @Override
    public IReviseStepsExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
