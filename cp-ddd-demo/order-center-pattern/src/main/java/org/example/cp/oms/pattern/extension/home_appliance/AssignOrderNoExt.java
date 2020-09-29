package org.example.cp.oms.pattern.extension.home_appliance;

import org.example.cp.oms.pattern.HomeAppliancePattern;
import org.cdf.ddd.annotation.Extension;
import org.example.cp.oms.spec.ext.IAssignOrderNoExt;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Extension(code = HomeAppliancePattern.CODE, value = "homeAssignOrderNoExt")
public class AssignOrderNoExt implements IAssignOrderNoExt {
    public static final String HOME_ORDER_NO = "SO9987012";

    @Override
    public void assignOrderNo(@NotNull IOrderModel model) {
        model.assignOrderNo(this, HOME_ORDER_NO);
    }
}
