package io.github.dddplus.runtime.registry.mock.step;

import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.step.IRevokableDomainStep;
import lombok.NonNull;

public abstract class SubmitStep implements IRevokableDomainStep<FooModel, FooException> {

    @Override
    public String activityCode() {
        return Steps.Submit.Activity;
    }

    @Override
    public void rollback(@NonNull FooModel model, @NonNull FooException cause) {
    }
}
