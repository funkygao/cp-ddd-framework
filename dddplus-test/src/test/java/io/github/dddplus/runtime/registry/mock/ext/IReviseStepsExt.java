package io.github.dddplus.runtime.registry.mock.ext;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import java.util.List;

public interface IReviseStepsExt extends IDomainExtension {

    List<String> reviseSteps(FooModel model);

}
