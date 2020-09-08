package org.cp.oms.pattern;

import org.x.cp.ddd.annotation.Pattern;
import org.x.cp.ddd.model.BasePattern;
import org.x.cp.ddd.model.IDomainModel;
import org.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Pattern(code = AmwayPattern.CODE, name = "安利业务模式")
public class AmwayPattern extends BasePattern {
    public static final String CODE = "amway";

    @Override
    public boolean match(@NotNull IDomainModel model) {
        if (!(model instanceof IOrderModel)) {
            return false;
        }

        IOrderModel orderModel = (IOrderModel) model;
        if (orderModel.getCustomerNo() == null) {
            return false;
        }
        return orderModel.getCustomerNo().equals("amway");
    }
}
