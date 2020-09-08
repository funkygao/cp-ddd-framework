package org.cp.oms.domain.step.submitorder;

import org.cp.oms.domain.exception.OrderException;
import org.cp.oms.domain.facade.mq.IMessageProducer;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.domain.step.SubmitOrderStep;
import org.cp.oms.spec.Steps;
import org.x.cp.ddd.annotation.Step;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Step(value = "submitMqStep")
public class BroadcastStep extends SubmitOrderStep {

    @Resource
    private IMessageProducer messageProducer;
    
    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {
        messageProducer.produce(model);
    }

    @Override
    public String stepCode() {
        return Steps.SubmitOrder.BroadcastStep;
    }
}
