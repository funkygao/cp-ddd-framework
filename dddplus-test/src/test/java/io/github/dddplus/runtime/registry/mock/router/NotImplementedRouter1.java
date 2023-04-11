package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.INotImplementedExt1;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Router(domain = FooDomain.CODE)
@Slf4j
public class NotImplementedRouter1 extends BaseRouter<INotImplementedExt1, FooModel> {

    public void ping(FooModel model) {
        firstExtension(model).doSth();
    }

    @Override
    public INotImplementedExt1 defaultExtension(@NonNull FooModel model) {
        return null;
    }
}
