package org.ddd.cp.ddd.runtime.registry.mock.ext;

import org.ddd.cp.ddd.ext.IDomainExtension;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import java.util.List;

public interface IReviseStepsExt extends IDomainExtension {

    List<String> reviseSteps(FooModel model);

}
