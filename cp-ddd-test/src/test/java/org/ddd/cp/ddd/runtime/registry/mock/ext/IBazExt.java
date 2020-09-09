package org.ddd.cp.ddd.runtime.registry.mock.ext;

import org.ddd.cp.ddd.ext.IDomainExtension;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

public interface IBazExt extends IDomainExtension {

    Integer execute(FooModel model);

}
