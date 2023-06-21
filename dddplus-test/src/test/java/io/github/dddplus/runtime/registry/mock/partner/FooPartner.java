package io.github.dddplus.runtime.registry.mock.partner;


import io.github.dddplus.annotation.Partner;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

@Partner(code = FooPartner.CODE, name = "BP::foo")
public class FooPartner implements IIdentityResolver<FooModel> {
    public static final String CODE = "ddd.cn.ka";

    @Override
    public boolean match(@NonNull FooModel identity) {
        return CODE.equals(identity.getPartnerCode());
    }
}
