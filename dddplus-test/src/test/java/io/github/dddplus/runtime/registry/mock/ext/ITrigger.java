package io.github.dddplus.runtime.registry.mock.ext;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

public interface ITrigger extends IDomainExtension {

    void beforeInsert(FooModel model);

    void afterInsert(FooModel model);
}
