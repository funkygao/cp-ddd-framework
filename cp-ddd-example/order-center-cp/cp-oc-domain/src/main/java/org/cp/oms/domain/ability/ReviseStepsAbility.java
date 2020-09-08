package org.cp.oms.domain.ability;

import org.x.cp.ddd.annotation.DomainAbility;
import org.x.cp.ddd.model.BaseDomainAbility;
import org.cp.oms.domain.CoreDomain;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.spec.ext.IReviseStepsExt;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = CoreDomain.CODE)
public class ReviseStepsAbility extends BaseDomainAbility<OrderModel, IReviseStepsExt> {

    public List<String> revisedSteps(@NotNull OrderModel model) {
        // execute ext with timeout 300ms
        return firstExtension(model, 300).reviseSteps(model);
    }

    @Override
    public IReviseStepsExt defaultExtension(@NotNull OrderModel model) {
        return null;
    }
}
