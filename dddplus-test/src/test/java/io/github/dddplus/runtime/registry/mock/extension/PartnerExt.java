package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.partner.FooPartner;
import io.github.dddplus.runtime.registry.mock.plugin.IXxxExtPlugin;
import lombok.extern.slf4j.Slf4j;

@Extension(code = FooPartner.CODE, name = "垂直业务实现的扩展点Foo项目")
@Slf4j
public class PartnerExt implements IFooExt {
    public static final int RESULT = 19;

    //@Resource
    private IXxxExtPlugin xxxExtPlugin;

    @Override
    public Integer execute(FooModel model) {
        log.info("partner");
        return RESULT;
    }
}
