package io.github.dddplus.runtime.registry.mock.step;

import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.step.IDomainStep;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

public abstract class CancelStep implements IDomainStep<FooModel, FooException> {

    @Override
    public String activityCode() {
        return Steps.Cancel.Activity;
    }
}
