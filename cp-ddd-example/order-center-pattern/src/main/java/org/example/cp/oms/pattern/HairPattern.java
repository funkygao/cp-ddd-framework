package org.example.cp.oms.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.example.cp.oms.spec.model.IOrderModel;
import org.ddd.cp.ddd.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = HairPattern.CODE, name = "海尔业务模式")
public class HairPattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = "hair";

    @Override
    public boolean match(@NotNull IOrderModel model) {
        if (model.getCustomerNo() == null) {
            return false;
        }

        return model.getCustomerNo().equals(CODE);
    }
}
