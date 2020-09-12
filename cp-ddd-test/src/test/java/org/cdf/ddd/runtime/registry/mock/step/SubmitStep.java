package org.cdf.ddd.runtime.registry.mock.step;

import org.cdf.ddd.runtime.registry.mock.exception.FooException;
import org.cdf.ddd.step.IDomainRevokableStep;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

public abstract class SubmitStep implements IDomainRevokableStep<FooModel, FooException> {

    @Override
    public String activityCode() {
        return Steps.Submit.Activity;
    }

    @Override
    public void rollback(@NotNull FooModel model, @NotNull FooException cause) {
    }
}
