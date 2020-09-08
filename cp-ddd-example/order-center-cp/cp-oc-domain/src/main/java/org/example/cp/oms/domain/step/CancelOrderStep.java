package org.example.cp.oms.domain.step;

import org.example.cp.oms.domain.exception.OrderException;
import org.example.cp.oms.domain.model.OrderModel;
import org.ddd.cp.ddd.model.IDomainStep;
import org.example.cp.oms.spec.Steps;

public abstract class CancelOrderStep implements IDomainStep<OrderModel, OrderException> {

    @Override
    public String activityCode() {
        return Steps.CancelOrder.Activity;
    }

}
