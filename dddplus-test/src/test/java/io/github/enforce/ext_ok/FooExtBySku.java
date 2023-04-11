package io.github.enforce.ext_ok;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Extension(code = "foo")
public class FooExtBySku implements IFooExt {
    @Override
    public Integer execute(FooModel model) {
        return null;
    }
}
