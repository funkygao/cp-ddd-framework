package org.example.cp.oms.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.example.cp.oms.spec.model.IOrderModel;
import org.ddd.cp.ddd.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = AmwayPattern.CODE, name = "安利业务模式")
public class AmwayPattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = "amway";

    @Override
    public boolean match(@NotNull IOrderModel model) {
        if (model.getCustomerNo() == null) {
            return false;
        }

        return model.getCustomerNo().equals("amway");
    }
}
