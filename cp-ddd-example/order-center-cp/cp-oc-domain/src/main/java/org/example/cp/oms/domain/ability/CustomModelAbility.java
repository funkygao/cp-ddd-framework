package org.example.cp.oms.domain.ability;

import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.ext.IModelAttachmentExt;
import org.cdf.ddd.model.BaseDomainAbility;
import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.model.IOrderModel;

@DomainAbility(domain = CoreDomain.CODE)
public class CustomModelAbility extends BaseDomainAbility<IOrderModel, IModelAttachmentExt> {

    @Override
    public IModelAttachmentExt defaultExtension(IOrderModel model) {
        return null;
    }
}
