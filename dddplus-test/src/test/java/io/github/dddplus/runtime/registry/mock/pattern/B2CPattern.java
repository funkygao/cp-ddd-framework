package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Pattern(code = B2CPattern.CODE, name = "B2C模式")
public class B2CPattern implements IIdentityResolver {
    public static final String CODE = "bar";

    @Override
    public boolean match(IIdentity model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return ((FooModel) model).isB2c();
    }
}
