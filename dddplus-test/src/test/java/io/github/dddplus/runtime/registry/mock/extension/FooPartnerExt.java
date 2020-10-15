package io.github.dddplus.runtime.registry.mock.extension;

import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.partner.FooPartner;
import io.github.dddplus.runtime.registry.mock.ext.IPartnerExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Extension(code = FooPartner.CODE, name = "垂直业务实现的扩展点Foo项目")
@Slf4j
public class FooPartnerExt implements IPartnerExt {
    public static final int RESULT = 29;

    @Override
    public Integer execute(FooModel model) {
        return RESULT;
    }
}
