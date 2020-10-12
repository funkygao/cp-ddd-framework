package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IBazExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Extension(code = IBazExt.DefaultCode)
public class DefaultBazExt implements IBazExt {
    @Override
    public Integer execute(FooModel model) {
        return 198;
    }
}
