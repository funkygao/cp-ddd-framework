package io.github.errcase.partner;


import io.github.dddplus.annotation.Partner;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Partner(code = DupFooPartner.CODE, name = "BP::foo")
public class DupFooPartner implements IIdentityResolver<FooModel> {
    public static final String CODE = "jdl.cn.ka";

    @Override
    public boolean match(@NotNull FooModel model) {
        return DupFooPartner.CODE.equals(model.getPartnerCode());
    }
}
