package io.github.enforce.router_naming;

import io.github.dddplus.annotation.Router;
import io.github.enforce.FooIdentity;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import lombok.NonNull;

@Router
public class XxxHandler extends BaseRouter<IFooExt, FooIdentity> {
    @Override
    public IFooExt defaultExtension(@NonNull FooIdentity identity) {
        return null;
    }
}
