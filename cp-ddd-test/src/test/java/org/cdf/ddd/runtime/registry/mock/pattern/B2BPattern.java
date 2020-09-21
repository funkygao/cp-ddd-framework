package org.cdf.ddd.runtime.registry.mock.pattern;

import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.ext.IIdentityResolver;
import org.cdf.ddd.model.IDomainModel;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Pattern(code = B2BPattern.CODE, name = "B2B模式", tags = PatternTags.B2B, priority = 90)
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
