package org.cp.oms.pattern;

import org.x.cp.ddd.annotation.Pattern;
import org.x.cp.ddd.model.BasePattern;
import org.x.cp.ddd.model.IDomainModel;
import org.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Pattern(code = SkyworthPattern.CODE, name = "创维业务模式")
public class SkyworthPattern extends BasePattern {
    public static final String CODE = "skyworth";

    @Override
    public boolean match(@NotNull IDomainModel model) {
        if (!(model instanceof IOrderModel)) {
            return false;
        }

        IOrderModel orderModel = (IOrderModel) model;
        if (orderModel.getCustomerNo() == null) {
            return false;
        }
        return orderModel.getCustomerNo().equals("skyworth");
    }
}
