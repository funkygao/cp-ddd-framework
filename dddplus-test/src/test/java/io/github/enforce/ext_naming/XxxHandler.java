package io.github.enforce.ext_naming;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Extension(code = "x")
public class XxxHandler implements IFooExt {
    @Override
    public Integer execute(FooModel model) {
        return null;
    }
}
