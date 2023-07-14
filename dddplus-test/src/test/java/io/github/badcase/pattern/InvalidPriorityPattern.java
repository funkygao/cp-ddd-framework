package io.github.badcase.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import lombok.NonNull;

@Pattern(code = InvalidPriorityPattern.CODE, name = "B2B模式", priority = -1)
public class InvalidPriorityPattern implements IIdentityResolver {
    public static final String CODE = "invalid";

    @Override
    public boolean match(@NonNull IIdentity model) {
        return false;
    }
}
