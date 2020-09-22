package org.example.cp.oms.domain.ability;

import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.model.BaseDomainAbility;
import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.ext.IPresortExt;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = CoreDomain.CODE, name = "预分拣的能力")
public class PresortAbility extends BaseDomainAbility<IOrderModel, IPresortExt> {

    public void presort(@NotNull IOrderModel model) {
        firstExtension(model).presort(model);
    }

    @Override
    public IPresortExt defaultExtension(@NotNull IOrderModel model) {
        return null;
    }
}
