package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Pattern(code = BarPattern.CODE, name = "bar模式")
public class BarPattern implements IIdentityResolver<IDomainModel> {
    public static final String CODE = "bar";

    @Override
    public boolean match(IDomainModel model) {
        if (!(model instanceof FooModel)) {
            return false;
        }

        return ((FooModel) model).isB2c();
    }
}
