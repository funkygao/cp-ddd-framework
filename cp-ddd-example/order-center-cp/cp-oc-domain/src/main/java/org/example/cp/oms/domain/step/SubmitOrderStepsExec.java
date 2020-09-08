package org.example.cp.oms.domain.step;

import org.x.cp.ddd.runtime.StepsExecTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.model.OrderModel;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SubmitOrderStepsExec extends StepsExecTemplate<SubmitOrderStep, OrderModel> {

    @Override
    protected void beforeStep(SubmitOrderStep step, OrderModel model) {
        log.info("step:{}.{} before:{}", step.activityCode(), step.stepCode(), model.label());
    }

    @Override
    protected void afterStep(SubmitOrderStep step, OrderModel model) {
    }
}
