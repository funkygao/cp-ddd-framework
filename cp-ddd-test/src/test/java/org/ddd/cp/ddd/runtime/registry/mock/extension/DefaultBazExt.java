package org.ddd.cp.ddd.runtime.registry.mock.extension;

import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IBazExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

@Extension(code = IBazExt.DefaultCode)
public class DefaultBazExt implements IBazExt {
    @Override
    public Integer execute(FooModel model) {
        return 198;
    }
}
