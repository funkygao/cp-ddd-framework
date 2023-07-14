package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

@Pattern(code = B2BPattern.CODE, name = "B2B模式", tags = Patterns.B2B, priority = 90)
public class B2BPattern implements IIdentityResolver {
    public static final String CODE = "b2b";

    @Override
    public boolean match(@NonNull IIdentity model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return !((FooModel) model).isB2c();
    }
}
