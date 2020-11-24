package io.github.errcase.policy;

import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IExtPolicy;
import io.github.dddplus.runtime.registry.mock.ext.ITrigger;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@Policy(extClazz = ITrigger.class)
public class DupTriggerPolicy implements IExtPolicy<FooModel> {
    @Override
    public String extensionCode(FooModel model) {
        return "foo";
    }
}
