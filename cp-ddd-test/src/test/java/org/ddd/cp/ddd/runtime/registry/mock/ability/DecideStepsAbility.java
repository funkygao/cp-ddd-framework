package org.ddd.cp.ddd.runtime.registry.mock.ability;

import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.ext.IDecideStepsExt;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

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
