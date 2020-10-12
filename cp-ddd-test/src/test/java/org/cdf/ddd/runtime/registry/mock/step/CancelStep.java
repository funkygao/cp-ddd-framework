package org.cdf.ddd.runtime.registry.mock.step;

import org.cdf.ddd.runtime.registry.mock.exception.FooException;
import org.cdf.ddd.step.IDomainStep;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

public abstract class CancelStep implements IDomainStep<FooModel, FooException> {

    @Override
    public String activityCode() {
        return Steps.Cancel.Activity;
    }
}
