package org.example.cp.oms.pattern;

import org.ddd.cp.ddd.annotation.Pattern;
import org.example.cp.oms.spec.model.IOrderModel;
import org.ddd.cp.ddd.ext.IIdentityResolver;

import javax.validation.constraints.NotNull;

@Pattern(code = SkyworthPattern.CODE, name = "创维业务模式")
public class SkyworthPattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = "skyworth";

    @Override
    public boolean match(@NotNull IOrderModel model) {
        if (model.getCustomerNo() == null) {
            return false;
        }

        return model.getCustomerNo().equals("skyworth");
    }
}
