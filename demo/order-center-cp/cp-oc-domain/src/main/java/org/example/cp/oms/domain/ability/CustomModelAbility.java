package org.example.cp.oms.domain.ability;

import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.ext.IModelAttachmentExt;
import org.cdf.ddd.runtime.BaseDomainAbility;
import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = CoreDomain.CODE)
public class CustomModelAbility extends BaseDomainAbility<IOrderModel, IModelAttachmentExt> {

    public void explain(@NotNull IOrderModel model) {
        firstExtension(model).explain(model.requestProfile(), model);
    }

    @Override
    public IModelAttachmentExt defaultExtension(IOrderModel model) {
        return null;
    }
}
