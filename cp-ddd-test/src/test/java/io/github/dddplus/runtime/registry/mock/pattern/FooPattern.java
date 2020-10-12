package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;

@Pattern(code = FooPattern.CODE, name = "foo模式", tags = Patterns.B2C)
public class FooPattern implements IIdentityResolver<IDomainModel> {
    public static final String CODE = "foo";

    @Override
    public boolean match(IDomainModel model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return ((FooModel) model).getProjectCode().equals("foo");
    }
}
