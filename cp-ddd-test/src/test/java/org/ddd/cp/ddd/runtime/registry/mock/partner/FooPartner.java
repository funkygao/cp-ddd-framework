package org.ddd.cp.ddd.runtime.registry.mock.partner;


import org.ddd.cp.ddd.annotation.Partner;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Partner(code = FooPartner.CODE, name = "BP::foo")
public class FooPartner implements IIdentityResolver<FooModel> {
    public static final String CODE = "jdl.cn.ka";

    @Override
    public boolean match(@NotNull FooModel model) {
        return FooPartner.CODE.equals(model.getProjectCode());
    }
}
