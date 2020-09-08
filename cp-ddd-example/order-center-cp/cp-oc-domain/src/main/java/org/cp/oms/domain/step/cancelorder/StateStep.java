package org.cp.oms.domain.step.cancelorder;

import lombok.extern.slf4j.Slf4j;
import org.cp.oms.domain.exception.OrderException;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.domain.step.CancelOrderStep;
import org.cp.oms.spec.Steps;
import org.x.cp.ddd.annotation.Step;

import javax.validation.constraints.NotNull;

@Step(value = "cancelStateStep", name = "订单状态校验")
@Slf4j
public class StateStep extends CancelOrderStep {

    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {

    }

    @Override
    public String stepCode() {
        return Steps.CancelOrder.StateStep;
    }
}
