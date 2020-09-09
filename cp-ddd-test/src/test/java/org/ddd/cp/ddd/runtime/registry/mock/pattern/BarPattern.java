package org.ddd.cp.ddd.runtime.registry.mock.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

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
