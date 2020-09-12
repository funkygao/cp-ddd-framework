package org.cdf.ddd.runtime.registry.mock.ext;

import org.cdf.ddd.ext.IDomainExtension;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import java.util.List;

public interface IReviseStepsExt extends IDomainExtension {

    List<String> reviseSteps(FooModel model);

}
