package org.example.cp.oms.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.example.cp.oms.spec.model.IOrderModel;
import org.ddd.cp.ddd.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = HomeAppliancePattern.CODE, name = "家用电器行业模式")
public class HomeAppliancePattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = "home";

    @Override
    public boolean match(@NotNull IOrderModel model) {
        if (model.getCustomerNo() == null) {
            return false;
        }

        return model.getCustomerNo().equals(CODE);
    }
}
