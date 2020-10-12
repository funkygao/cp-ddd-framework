package io.github.dddplus.runtime.registry.mock.ext;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

public interface IBazExt extends IDomainExtension {

    Integer execute(FooModel model);

}
