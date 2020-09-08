package org.bp.oms.ka;

import org.cp.oms.spec.Partners;
import org.cp.oms.spec.resolver.KaResolver;
import org.x.cp.ddd.annotation.Partner;

@Partner(
        code = KaPartner.CODE,
        resolverClass = KaResolver.class,
        name = "KA业务前台"
)
public class KaPartner {
    public static final String CODE = Partners.KA;
}
