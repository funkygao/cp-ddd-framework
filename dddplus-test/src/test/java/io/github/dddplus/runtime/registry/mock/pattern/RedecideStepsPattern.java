package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

@Pattern(code = RedecideStepsPattern.CODE, name = "重新编排后续步骤")
public class RedecideStepsPattern implements IIdentityResolver {
    public static final String CODE = "revise";

    @Override
    public boolean match(@NonNull IIdentity model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return ((FooModel) model).isRedecide();
    }
}
