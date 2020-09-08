package org.cp.oms.domain.step.submitorder;

import org.x.cp.ddd.annotation.Step;
import org.x.cp.ddd.runtime.DDD;
import lombok.extern.slf4j.Slf4j;
import org.cp.oms.domain.ability.ReviseStepsAbility;
import org.cp.oms.domain.exception.OrderException;
import org.cp.oms.domain.exception.OrderReviseStepsException;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.domain.step.SubmitOrderStep;
import org.cp.oms.spec.Steps;

import javax.validation.constraints.NotNull;
import java.util.List;

@Step(value = "submitBasicStep")
@Slf4j
public class BasicStep extends SubmitOrderStep {

    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {
        model.setStep(this.stepCode());
        List<String> revisedSteps = DDD.findAbility(ReviseStepsAbility.class).revisedSteps(model);
        if (revisedSteps != null) {
            log.info("重新编排步骤：{}", revisedSteps);

            // 通过异常，来改变后续步骤
            throw new OrderReviseStepsException().withSubsequentSteps(revisedSteps);
        }
    }

    @Override
    public void rollback(@NotNull OrderModel model, @NotNull OrderException cause) {
        log.info("will rollback now...");
    }

    @Override
    public String stepCode() {
        return Steps.SubmitOrder.BasicStep;
    }
}
