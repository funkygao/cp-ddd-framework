package org.ddd.cp.ddd.runtime.registry.mock.extension;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.pattern.FooPattern;

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
