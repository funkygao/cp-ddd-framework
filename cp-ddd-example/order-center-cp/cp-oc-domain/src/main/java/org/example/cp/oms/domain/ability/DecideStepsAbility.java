package org.example.cp.oms.domain.ability;

import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.model.IOrderModel;
import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.ext.IDecideStepsExt;
import org.ddd.cp.ddd.model.BaseDomainAbility;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = CoreDomain.CODE, name = "动态决定领域步骤的能力")
public class DecideStepsAbility extends BaseDomainAbility<IOrderModel, IDecideStepsExt> {

    public List<String> decideSteps(@NotNull IOrderModel model, String activityCode) {
        return firstExtension(model).decideSteps(model, activityCode);
    }

    @Override
    public IDecideStepsExt defaultExtension(@NotNull IOrderModel model) {
        // 没有默认的扩展点实现
        return null;
    }
}
