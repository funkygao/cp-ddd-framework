package org.ddd.cp.ddd.runtime.registry.mock.step;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.Step;
import org.ddd.cp.ddd.runtime.registry.mock.exception.FooException;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Step
@Slf4j
public class HamStep extends SubmitStep {

    @Override
    public void execute(@NotNull FooModel model) throws FooException {
        log.info("submit: {}", model);
        // 为了assert走到这个步骤
        model.setStepsRevised(true);
    }

    @Override
    public String stepCode() {
        return Steps.Submit.HamStep;
    }
}
