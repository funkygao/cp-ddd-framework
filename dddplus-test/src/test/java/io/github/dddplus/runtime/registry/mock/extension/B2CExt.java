package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.pattern.B2CPattern;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Extension(code = B2CPattern.CODE, name = "水平扩展点Bar模式")
@Slf4j
public class B2CExt implements IFooExt {
    public static final int RESULT = 2;

    @Override
    public Integer execute(FooModel model) {
        log.info("2");
        return RESULT;
    }
}
