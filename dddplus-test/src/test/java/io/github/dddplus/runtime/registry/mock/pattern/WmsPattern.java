package io.github.dddplus.runtime.registry.mock.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.runtime.registry.mock.model.SaleOrder;

@Pattern(code = WmsPattern.CODE)
public class WmsPattern implements IIdentityResolver<SaleOrder> {
    public static final String CODE = "WMS";

    public boolean match(SaleOrder identity) {
        return true;
    }
}
