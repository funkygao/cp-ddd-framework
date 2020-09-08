package org.cp.oms.domain.service;

import org.x.cp.ddd.annotation.DomainService;
import org.x.cp.ddd.model.IDomainService;
import org.x.cp.ddd.runtime.DDD;
import lombok.extern.slf4j.Slf4j;
import org.cp.oms.domain.CoreDomain;
import org.cp.oms.domain.ability.DecideStepsAbility;
import org.cp.oms.domain.ability.SerializableIsolationAbility;
import org.cp.oms.domain.exception.OrderException;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.domain.step.SubmitOrderStepsExec;
import org.cp.oms.spec.Steps;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.locks.Lock;

@DomainService(domain = CoreDomain.CODE)
@Slf4j
public class SubmitOrder implements IDomainService {

    @Resource
    private SubmitOrderStepsExec submitOrderStepsExec;

    public void submit(@NotNull OrderModel orderModel) throws OrderException {
        Lock lock = DDD.findAbility(SerializableIsolationAbility.class).acquireLock(orderModel);
        if (SerializableIsolationAbility.useLock(lock) && !lock.tryLock()) {
            // 存在并发
            throw new OrderException();
        }

        List<String> steps = DDD.findAbility(DecideStepsAbility.class).decideSteps(orderModel, Steps.SubmitOrder.Activity);
        submitOrderStepsExec.execute(Steps.SubmitOrder.Activity, steps, orderModel);
    }
}
