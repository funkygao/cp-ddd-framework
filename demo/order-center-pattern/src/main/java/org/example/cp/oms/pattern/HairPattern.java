package org.example.cp.oms.pattern;

import io.github.dddplus.annotation.Pattern;
import org.example.cp.oms.spec.Patterns;
import org.example.cp.oms.spec.model.IOrderModel;
import io.github.dddplus.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = HairPattern.CODE, name = "海尔业务模式")
public class HairPattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = Patterns.Hair;

    @Override
    public boolean match(@NotNull IOrderModel model) {
        if (model.getCustomerNo() == null) {
            return false;
        }

        return model.getCustomerNo().equals(CODE);
    }
}
