package org.cdf.ddd.runtime.registry.mock.step;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Step;
import org.cdf.ddd.runtime.registry.mock.exception.FooException;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Step
@Slf4j
public class EggStep extends CancelStep {

    @Override
    public void execute(@NotNull FooModel model) throws FooException {
        log.info("submit: {}", model);
    }

    @Override
    public String stepCode() {
        return Steps.Cancel.EggStep;
    }
}
