package io.github.dddplus.runtime.registry.mock.ability;

import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.ext.IDecideStepsExt;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = FooDomain.CODE, value = "mockDecideStepsAbility", tags = Abilities.decideSteps)
public class DecideStepsAbility extends BaseDomainAbility<FooModel, IDecideStepsExt> {

    public List<String> decideSteps(@NotNull FooModel model, String activityCode) {
        return firstExtension(model).decideSteps(model, activityCode);
    }

    @Override
    public IDecideStepsExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
