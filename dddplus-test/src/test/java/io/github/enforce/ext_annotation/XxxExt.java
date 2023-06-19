package io.github.enforce.ext_annotation;

import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

public class XxxExt implements IFooExt {
    @Override
    public Integer execute(FooModel model) {
        return null;
    }
}
