package org.cp.oms.domain.step.submitorder;

import org.cp.oms.domain.ability.AssignOrderNoAbility;
import org.cp.oms.domain.exception.OrderException;
import org.cp.oms.domain.facade.repository.IOrderRepository;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.domain.step.SubmitOrderStep;
import org.cp.oms.spec.Steps;
import org.x.cp.ddd.annotation.Step;
import org.x.cp.ddd.runtime.DDD;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Step(value = "submitPersistStep")
public class PersistStep extends SubmitOrderStep {

    @Resource
    private IOrderRepository orderRepository;
    
    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {
        DDD.findAbility(AssignOrderNoAbility.class).assignOrderNo(model);
        orderRepository.persist(model);
    }

    @Override
    public String stepCode() {
        return Steps.SubmitOrder.PersistStep;
    }
}
