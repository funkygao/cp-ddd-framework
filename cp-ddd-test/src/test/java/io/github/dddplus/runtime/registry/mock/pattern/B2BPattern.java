package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = B2BPattern.CODE, name = "B2B模式", tags = Patterns.B2B, priority = 90)
public class B2BPattern implements IIdentityResolver<IDomainModel> {
    public static final String CODE = "b2b";

    @Override
    public boolean match(@NotNull IDomainModel model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return !((FooModel) model).isB2c();
    }
}
