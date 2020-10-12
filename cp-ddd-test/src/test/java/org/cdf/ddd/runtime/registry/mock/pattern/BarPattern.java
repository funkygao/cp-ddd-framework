package org.cdf.ddd.runtime.registry.mock.pattern;

import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.ext.IIdentityResolver;
import org.cdf.ddd.model.IDomainModel;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

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
