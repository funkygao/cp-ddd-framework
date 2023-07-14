package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Pattern(code = FooPattern.CODE, name = "foo模式", tags = Patterns.B2C)
public class FooPattern implements IIdentityResolver {
    public static final String CODE = "foo";

    @Override
    public boolean match(IIdentity model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return ((FooModel) model).getPartnerCode().equals("foo");
    }
}
