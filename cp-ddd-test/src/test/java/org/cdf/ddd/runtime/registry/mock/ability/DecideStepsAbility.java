package org.cdf.ddd.runtime.registry.mock.ability;

import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.ext.IDecideStepsExt;
import org.cdf.ddd.model.BaseDomainAbility;
import org.cdf.ddd.runtime.registry.mock.domain.FooDomain;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = FooDomain.CODE, value = "mockDecideStepsAbility")
public class DecideStepsAbility extends BaseDomainAbility<FooModel, IDecideStepsExt> {

    public List<String> decideSteps(@NotNull FooModel model, String activityCode) {
        return firstExtension(model).decideSteps(model, activityCode);
    }

    @Override
    public IDecideStepsExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
