package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IReviseStepsExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@Router(domain = FooDomain.CODE, value = "mockReviseStepsAbility", tags = RouterTag.reviseSteps)
public class ReviseStepsRouter extends BaseRouter<FooModel, IReviseStepsExt> {

    public List<String> revisedSteps(@NotNull FooModel model) {
        return firstExtension(model).reviseSteps(model);
    }

    @Override
    public IReviseStepsExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
