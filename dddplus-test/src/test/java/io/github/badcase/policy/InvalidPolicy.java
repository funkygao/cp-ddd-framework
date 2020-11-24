package io.github.badcase.policy;

import io.github.dddplus.annotation.Policy;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;

@Policy(extClazz = IFooExt.class)
public class InvalidPolicy {
    // policy must implement IExtPolicy
}
