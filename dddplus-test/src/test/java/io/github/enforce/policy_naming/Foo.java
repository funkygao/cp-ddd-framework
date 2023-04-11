package io.github.enforce.policy_naming;

import io.github.dddplus.annotation.Policy;
import io.github.enforce.FooIdentity;
import io.github.dddplus.ext.IPolicy;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import lombok.NonNull;

@Policy
public class Foo implements IPolicy<IFooExt, FooIdentity> {
    @Override
    public @NonNull String extensionCode(@NonNull FooIdentity identity) {
        return null;
    }
}
