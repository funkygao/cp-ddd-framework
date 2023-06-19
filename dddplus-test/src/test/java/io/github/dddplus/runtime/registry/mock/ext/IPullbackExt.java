package io.github.dddplus.runtime.registry.mock.ext;

import io.github.dddplus.ext.IDomainExtension;

public interface IPullbackExt extends IDomainExtension {

    void pullback();
}
