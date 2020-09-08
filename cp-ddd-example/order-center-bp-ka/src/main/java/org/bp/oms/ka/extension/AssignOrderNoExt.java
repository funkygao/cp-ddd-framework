package org.bp.oms.ka.extension;

import org.bp.oms.ka.KaPartner;
import org.cp.oms.spec.ext.IAssignOrderNoExt;
import org.cp.oms.spec.model.IOrderModel;
import org.x.cp.ddd.annotation.Extension;

import javax.validation.constraints.NotNull;

@Extension(code = KaPartner.CODE, value = "kaAssignOrderNoExt")
public class AssignOrderNoExt implements IAssignOrderNoExt {
    public static final String KA_ORDER_NO = "KA1012";

    @Override
    public void assignOrderNo(@NotNull IOrderModel model) {
        model.assignOrderNo(this, KA_ORDER_NO);
    }
}
