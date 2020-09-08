package org.cp.oms.spec.resolver;

import org.cp.oms.spec.model.IOrderModel;
import org.x.cp.ddd.model.IDomainModel;
import org.x.cp.ddd.model.IPartnerResolver;

import javax.validation.constraints.NotNull;

public class KaResolver implements IPartnerResolver {

    @Override
    public boolean match(@NotNull IDomainModel model) {
        if (!(model instanceof IDomainModel)) {
            return false;
        }

        IOrderModel orderModel = (IOrderModel) model;
        if (orderModel.getSource() == null) {
            return false;
        }
        return orderModel.getSource().equalsIgnoreCase("KA");
    }
}
