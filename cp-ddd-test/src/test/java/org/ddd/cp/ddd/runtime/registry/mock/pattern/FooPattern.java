package org.ddd.cp.ddd.runtime.registry.mock.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

@Pattern(code = FooPattern.CODE, name = "foo模式")
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
