package io.github.errcase.partner;

import io.github.dddplus.annotation.Partner;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

@Partner(code = DupFooPartner.CODE, name = "BP::foo")
public class DupFooPartner implements IIdentityResolver<FooModel> {
    public static final String CODE = "ddd.cn.ka";

    @Override
    public boolean match(@NonNull FooModel identity) {
        return DupFooPartner.CODE.equals(identity.getPartnerCode());
    }
}
