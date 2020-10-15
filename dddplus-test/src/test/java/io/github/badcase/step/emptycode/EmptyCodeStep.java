package io.github.badcase.step.emptycode;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.step.SubmitStep;

@Step
public class EmptyCodeStep extends SubmitStep {
    @Override
    public void execute(FooModel model) throws FooException {

    }

    @Override
    public String stepCode() {
        return null;
    }
}
