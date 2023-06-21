package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Governance;
import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IBazExt;
import io.github.dddplus.runtime.registry.mock.extension.DefaultBazExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Router(domain = FooDomain.CODE, tags = {RouterTag.bar, RouterTag.foo})
@Slf4j
public class BazRouter extends BaseRouter<IBazExt, FooModel> {

    @Resource
    private DefaultBazExt defaultBazExt;

    @Governance(profiler = true)
    public Integer guess(FooModel model) {
        return firstExtension(model).execute(model);
    }

    @Override
    public IBazExt defaultExtension(@NonNull FooModel model) {
        return defaultBazExt;
    }
}
