package org.example.cp.oms.pattern.extension.skyworth;

import org.example.cp.oms.pattern.SkyworthPattern;
import org.x.cp.ddd.annotation.Extension;
import org.example.cp.oms.spec.ext.IAssignOrderNoExt;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Extension(code = SkyworthPattern.CODE, value = "skyworthAssignOrderNoExt")
public class AssignOrderNoExt implements IAssignOrderNoExt {
    public static final String SKYWORTH_ORDER_NO = "sky9987012";

    @Override
    public void assignOrderNo(@NotNull IOrderModel model) {
        model.assignOrderNo(this, SKYWORTH_ORDER_NO);
    }
}
