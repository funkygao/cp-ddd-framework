package io.github.dddplus.runtime.registry.mock.ability;

import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.runtime.BaseDecideStepsAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@DomainAbility(domain = FooDomain.CODE, value = "mockDecideStepsAbility", tags = AbilityTag.decideSteps)
public class DecideStepsAbility extends BaseDecideStepsAbility<FooModel> {
}
