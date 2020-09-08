package org.cp.oms.domain.step;

import org.x.cp.ddd.model.IDomainStep;
import org.cp.oms.domain.exception.OrderException;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.spec.Steps;

public abstract class CancelOrderStep implements IDomainStep<OrderModel, OrderException> {

    @Override
    public String activityCode() {
        return Steps.CancelOrder.Activity;
    }

}
