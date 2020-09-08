package org.example.cp.oms.domain.step.cancelorder;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.exception.OrderException;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.step.CancelOrderStep;
import org.example.cp.oms.spec.Steps;
import org.ddd.cp.ddd.annotation.Step;

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
