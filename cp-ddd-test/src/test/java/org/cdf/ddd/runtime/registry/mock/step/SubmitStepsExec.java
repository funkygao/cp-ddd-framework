package org.cdf.ddd.runtime.registry.mock.step;

import org.cdf.ddd.runtime.StepsExecTemplate;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SubmitStepsExec extends StepsExecTemplate<SubmitStep, FooModel> {

    @Override
    protected void beforeStep(SubmitStep step, FooModel model) {
        log.info("before:{}", model);
    }

    @Override
    protected void afterStep(SubmitStep step, FooModel model) {
        log.info("after:{}", model);
    }
}
