package org.ddd.cp.ddd.runtime.registry.mock.extension;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.pattern.BarPattern;

@Extension(code = BarPattern.CODE, name = "水平扩展点Bar模式")
@Slf4j
public class BarExt implements IFooExt {
    public static final int RESULT = 2;

    @Override
    public Integer execute(FooModel model) {
        log.info("2");
        return RESULT;
    }
}
