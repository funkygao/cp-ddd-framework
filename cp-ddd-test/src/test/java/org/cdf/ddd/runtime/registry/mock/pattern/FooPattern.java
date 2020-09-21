package org.cdf.ddd.runtime.registry.mock.pattern;

import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.ext.IIdentityResolver;
import org.cdf.ddd.model.IDomainModel;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

@Pattern(code = FooPattern.CODE, name = "foo模式", tags = PatternTags.B2C)
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
