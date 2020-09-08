package org.example.cp.oms.domain.ability;

import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.ext.IAssignOrderNoExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.x.cp.ddd.annotation.DomainAbility;
import org.x.cp.ddd.model.BaseDomainAbility;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = CoreDomain.CODE, name = "分配订单号的能力")
public class AssignOrderNoAbility extends BaseDomainAbility<IOrderModel, IAssignOrderNoExt> {

    public void assignOrderNo(@NotNull IOrderModel model) {
        firstExtension(model).assignOrderNo(model);
    }

    @Override
    public IAssignOrderNoExt defaultExtension(@NotNull IOrderModel model) {
        // 没有默认的扩展点实现
        return null;
    }
}
