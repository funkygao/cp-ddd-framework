package org.example.cp.oms.domain.step.submitorder;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.exception.OrderException;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.step.SubmitOrderStep;
import org.example.cp.oms.spec.Steps;
import org.cdf.ddd.annotation.Step;

import javax.validation.constraints.NotNull;

@Step(value = "submitProductStep", name = "订单里产品校验")
@Slf4j
public class ProductStep extends SubmitOrderStep {

    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {
    }

    @Override
    public String stepCode() {
        return Steps.SubmitOrder.ProductStep;
    }
}
