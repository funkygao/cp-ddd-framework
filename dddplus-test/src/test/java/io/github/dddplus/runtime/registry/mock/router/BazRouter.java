package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IBazExt;
import io.github.dddplus.runtime.registry.mock.extension.DefaultBazExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Router(domain = FooDomain.CODE, tags = {RouterTag.bar, RouterTag.foo})
@Slf4j
public class BazRouter extends BaseRouter<FooModel, IBazExt> {

    @Resource
    private DefaultBazExt defaultBazExt;

    public Integer guess(FooModel model) {
        return firstExtension(model).execute(model);
    }

    @Override
    public IBazExt defaultExtension(@NotNull FooModel model) {
        return defaultBazExt;
    }
}
