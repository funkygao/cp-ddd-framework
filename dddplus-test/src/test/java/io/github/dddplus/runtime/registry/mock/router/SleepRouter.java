package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.ISleepExt;
import io.github.dddplus.runtime.registry.mock.extension.DefaultSleepExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

import javax.annotation.Resource;

@Router(domain = FooDomain.CODE, name = "sleep")
public class SleepRouter extends BaseRouter<ISleepExt, FooModel> {

    @Resource
    private DefaultSleepExt defaultSleepExt;

    public void sleepTooLong(FooModel model) {
        // will always throw ExtTimeoutException
        firstExtension(model, 100).sleep(1);
    }


    @Override
    public ISleepExt defaultExtension(@NonNull FooModel model) {
        return defaultSleepExt;
    }
}
