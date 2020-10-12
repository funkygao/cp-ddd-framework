package org.cdf.ddd.runtime.registry.mock.extension;

import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.runtime.registry.mock.ext.IBazExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

// 测试，DefaultCode重复，是可以启动的
@Extension(code = IBazExt.DefaultCode)
public class Default1BazExt implements IBazExt {
    @Override
    public Integer execute(FooModel model) {
        return 198;
    }
}
