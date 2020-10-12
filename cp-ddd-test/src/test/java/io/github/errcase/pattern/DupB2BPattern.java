package io.github.errcase.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Pattern(code = DupB2BPattern.CODE, name = "B2B模式", priority = 90)
public class DupB2BPattern implements IIdentityResolver<IDomainModel> {
    public static final String CODE = "b2b";

    @Override
    public boolean match(@NotNull IDomainModel model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return !((FooModel) model).isB2c();
    }
}
