package io.github.enforce.policy_annotation;

import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.enforce.FooIdentity;
import io.github.dddplus.ext.IPolicy;
import lombok.NonNull;

public class FooPolicy implements IPolicy<IFooExt, FooIdentity> {
    @Override
    public @NonNull String extensionCode(@NonNull FooIdentity identity) {
        return null;
    }
}
