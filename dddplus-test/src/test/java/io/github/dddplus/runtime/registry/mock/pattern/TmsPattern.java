package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.runtime.registry.mock.model.SaleOrder;

@Pattern(code = TmsPattern.CODE)
public class TmsPattern implements IIdentityResolver<SaleOrder> {
    public static final String CODE = "TMS";

    public boolean match(SaleOrder identity) {
        return true;
    }
}
