package io.github.enforce.identity_resolver1;

import io.github.enforce.FooIdentity;
import io.github.dddplus.ext.IIdentityResolver;
import lombok.NonNull;

public class IllegalIdentityResolver implements IIdentityResolver<FooIdentity> {
    @Override
    public boolean match(@NonNull FooIdentity identity) {
        return false;
    }
}
