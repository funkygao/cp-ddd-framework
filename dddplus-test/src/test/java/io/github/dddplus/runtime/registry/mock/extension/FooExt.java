package io.github.dddplus.runtime.registry.mock.extension;

import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.pattern.FooPattern;

@Extension(code = FooPattern.CODE, name = "foo")
@Slf4j
public class FooExt implements IFooExt {
    public static final int RESULT = 1;

    @Override
    public Integer execute(FooModel model) {
        log.info("1");
        return RESULT;
    }
}
