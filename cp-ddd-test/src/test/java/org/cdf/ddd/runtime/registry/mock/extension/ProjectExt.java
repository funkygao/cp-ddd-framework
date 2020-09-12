package org.cdf.ddd.runtime.registry.mock.extension;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.runtime.registry.mock.partner.FooPartner;
import org.cdf.ddd.runtime.registry.mock.ext.IFooExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;
import org.cdf.ddd.runtime.registry.mock.plugin.XxxExtPlugin;

@Extension(code = FooPartner.CODE, name = "垂直业务实现的扩展点Foo项目")
@Slf4j
public class ProjectExt implements IFooExt {
    public static final int RESULT = 19;

    //@Resource
    private XxxExtPlugin xxxExtPlugin;

    @Override
    public Integer execute(FooModel model) {
        log.info("partner");
        return RESULT;
    }
}
