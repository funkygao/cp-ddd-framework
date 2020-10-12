package org.example.cp.oms.domain.step;

import org.example.cp.oms.domain.model.OrderModel;
import io.github.dddplus.step.IRevokableDomainStep;
import org.example.cp.oms.spec.exception.OrderException;
import org.example.cp.oms.spec.Steps;

import javax.validation.constraints.NotNull;

public abstract class SubmitOrderStep implements IRevokableDomainStep<OrderModel, OrderException> {

    @Override
    public String activityCode() {
        return Steps.SubmitOrder.Activity;
    }

    @Override
    public void rollback(@NotNull OrderModel model, @NotNull OrderException cause) {
        // 默认不回滚，子类可以通过覆盖实现对应步骤的回滚逻辑
    }
}
