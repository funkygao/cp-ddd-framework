package io.github.dddplus.runtime.registry.mock.ext;

import io.github.dddplus.ext.IDomainExtension;

public interface ISleepExt extends IDomainExtension {

    void sleep(int seconds);
}
