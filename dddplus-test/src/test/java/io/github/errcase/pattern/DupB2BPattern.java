package io.github.errcase.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

@Pattern(code = DupB2BPattern.CODE, name = "B2B模式", priority = 90)
public class DupB2BPattern implements IIdentityResolver {
    public static final String CODE = "b2b";

    @Override
    public boolean match(@NonNull IIdentity identity) {
        if (!(identity instanceof FooModel)) {
            return false;
        }

        return !((FooModel) identity).isB2c();
    }
}
