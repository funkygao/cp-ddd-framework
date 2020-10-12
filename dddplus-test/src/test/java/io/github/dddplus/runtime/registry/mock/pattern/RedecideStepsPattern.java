package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Pattern(code = RedecideStepsPattern.CODE, name = "重新编排后续步骤")
public class RedecideStepsPattern implements IIdentityResolver<IDomainModel> {
    public static final String CODE = "revise";

    @Override
    public boolean match(@NotNull IDomainModel model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return ((FooModel) model).isRedecide();
    }
}
