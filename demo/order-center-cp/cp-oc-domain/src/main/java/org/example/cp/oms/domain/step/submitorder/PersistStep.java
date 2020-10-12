package org.example.cp.oms.domain.step.submitorder;

import org.example.cp.oms.domain.ability.AssignOrderNoAbility;
import org.example.cp.oms.domain.ability.CustomModelAbility;
import org.example.cp.oms.spec.exception.OrderException;
import org.example.cp.oms.domain.facade.repository.IOrderRepository;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.step.SubmitOrderStep;
import org.example.cp.oms.spec.Steps;
import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.DDD;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Step(value = "submitPersistStep")
public class PersistStep extends SubmitOrderStep {

    @Resource
    private IOrderRepository orderRepository;
    
    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {
        // 分配订单号：不同场景下，订单号规则不同
        DDD.findAbility(AssignOrderNoAbility.class).assignOrderNo(model);

        // 处理个性化字段
        DDD.findAbility(CustomModelAbility.class).explain(model);

        // 落库
        orderRepository.persist(model);
    }

    @Override
    public String stepCode() {
        return Steps.SubmitOrder.PersistStep;
    }
}
