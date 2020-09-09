package org.ddd.cp.ddd.runtime.registry.mock.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

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
