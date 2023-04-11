package io.github.enforce.router_annotation;

import io.github.enforce.FooIdentity;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import lombok.NonNull;

public class FooRouter extends BaseRouter<IFooExt, FooIdentity> {
    @Override
    public IFooExt defaultExtension(@NonNull FooIdentity identity) {
        return null;
    }
}
