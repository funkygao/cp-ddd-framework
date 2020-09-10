package org.ddd.cp.ddd.runtime.registry.mock.extension;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IProjectExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.partner.FooPartner;

@Extension(code = FooPartner.CODE, name = "垂直业务实现的扩展点Foo项目")
@Slf4j
public class FooProjectExt implements IProjectExt {
    public static final int RESULT = 29;

    @Override
    public Integer execute(FooModel model) {
        return RESULT;
    }
}
