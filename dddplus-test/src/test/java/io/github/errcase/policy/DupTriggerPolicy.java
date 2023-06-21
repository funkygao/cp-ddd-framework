package io.github.errcase.policy;

import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IPolicy;
import io.github.dddplus.runtime.registry.mock.ext.ITrigger;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Policy
public class DupTriggerPolicy implements IPolicy<ITrigger, FooModel> {
    @Override
    public String extensionCode(FooModel model) {
        return "foo";
    }
}
