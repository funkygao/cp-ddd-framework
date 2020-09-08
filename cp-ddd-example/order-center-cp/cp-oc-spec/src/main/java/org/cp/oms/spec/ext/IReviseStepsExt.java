package org.cp.oms.spec.ext;

import org.cp.oms.spec.model.IOrderModel;
import org.x.cp.ddd.model.IDomainExtension;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IReviseStepsExt extends IDomainExtension {

    List<String> reviseSteps(@NotNull IOrderModel model);

}