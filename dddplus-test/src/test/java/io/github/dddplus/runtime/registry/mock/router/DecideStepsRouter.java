package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseDecideStepsRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Router(domain = FooDomain.CODE, value = "mockDecideStepsAbility", tags = RouterTag.decideSteps)
public class DecideStepsRouter extends BaseDecideStepsRouter<FooModel> {
}
