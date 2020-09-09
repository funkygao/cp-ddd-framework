package org.ddd.cp.ddd.runtime.registry.mock.step;

import org.ddd.cp.ddd.step.IDomainStep;
import org.ddd.cp.ddd.runtime.registry.mock.exception.FooException;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

public abstract class CancelStep implements IDomainStep<FooModel,FooException> {

    @Override
    public String activityCode() {
        return Steps.Cancel.Activity;
    }
}
