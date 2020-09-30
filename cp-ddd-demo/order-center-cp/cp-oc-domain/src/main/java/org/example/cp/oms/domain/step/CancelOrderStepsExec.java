package org.example.cp.oms.domain.step;

import org.cdf.ddd.runtime.StepsExecTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.model.OrderModel;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CancelOrderStepsExec extends StepsExecTemplate<CancelOrderStep, OrderModel> {

    @Override
    protected void beforeStep(CancelOrderStep step, OrderModel model) {
        log.info("step:{}.{} before:{}", step.activityCode(), step.stepCode(), model.label());
    }

    @Override
    protected void afterStep(CancelOrderStep step, OrderModel model) {
    }
}
