package io.github.badcase.step.emptyactivity;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.step.SubmitStep;

@Step
public class EmptyActivityStep extends SubmitStep {
    @Override
    public void execute(FooModel model) throws FooException {

    }

    @Override
    public String activityCode() {
        return "";
    }

    @Override
    public String stepCode() {
        return null;
    }
}
