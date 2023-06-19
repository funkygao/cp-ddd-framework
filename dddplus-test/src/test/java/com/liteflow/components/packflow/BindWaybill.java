package com.liteflow.components.packflow;

import com.liteflow.components.packflow.context.PackFlowContext;
import com.liteflow.components.packflow.domain.ShipmentOrder;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("BindWaybill")
public class BindWaybill extends NodeComponent {
    @Override
    public void process() throws Exception {
        PackFlowContext context = getFirstContextBean();
        ShipmentOrder so = context.getSo();
    }
}
