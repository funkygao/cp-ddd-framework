package org.example.cp.oms.pattern;

import io.github.dddplus.annotation.Pattern;
import org.example.cp.oms.spec.Patterns;
import org.example.cp.oms.spec.model.IOrderModel;
import io.github.dddplus.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = HomeAppliancePattern.CODE, name = "家用电器行业模式")
public class HomeAppliancePattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = Patterns.HomeAppliance;

    @Override
    public boolean match(@NotNull IOrderModel model) {
        if (model.getCustomerNo() == null) {
            return false;
        }

        return model.getCustomerNo().equals(CODE);
    }
}
