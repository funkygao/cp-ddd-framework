package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IReviseStepsExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

import java.util.List;

@Router(domain = FooDomain.CODE, value = "mockReviseStepsAbility", tags = RouterTag.reviseSteps)
public class ReviseStepsRouter extends BaseRouter<IReviseStepsExt, FooModel> {

    public List<String> revisedSteps(@NonNull FooModel model) {
        return firstExtension(model).reviseSteps(model);
    }

    @Override
    public IReviseStepsExt defaultExtension(@NonNull FooModel model) {
        return null;
    }
}
