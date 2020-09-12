package org.example.cp.oms.spec.partner;

import org.example.cp.oms.spec.model.IOrderModel;
import org.cdf.ddd.annotation.Partner;
import org.cdf.ddd.ext.IIdentityResolver;

@Partner(code = KaPartner.CODE, name = "KA业务前台")
public class KaPartner implements IIdentityResolver<IOrderModel> {
    public static final String CODE = "KA";

    @Override
    public boolean match(IOrderModel model) {
        if (model.getSource() == null) {
            return false;
        }

        return model.getSource().equalsIgnoreCase(CODE);
    }
}
