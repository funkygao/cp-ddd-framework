package org.bp.oms.isv;

import org.cp.oms.spec.Partners;
import org.cp.oms.spec.resolver.IsvResolver;
import org.x.cp.ddd.annotation.Partner;

@Partner(
        code = IsvPartner.CODE,
        resolverClass = IsvResolver.class,
        name = "ISV业务前台"
)
public class IsvPartner {
    public static final String CODE = Partners.ISV;
}
