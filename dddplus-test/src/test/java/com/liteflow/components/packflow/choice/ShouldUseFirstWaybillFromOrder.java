package com.liteflow.components.packflow.choice;

import com.liteflow.components.packflow.context.PackFlowContext;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;

@LiteflowComponent("ShouldUseFirstWaybillFromOrder")
public class ShouldUseFirstWaybillFromOrder extends NodeSwitchComponent {
    @Override
    public String processSwitch() throws Exception {
        PackFlowContext context = this.getFirstContextBean();
        Integer packageQty = getRequestData();
        if (context.getSo().needFirstWaybillFromOrder(packageQty)) {
            return "yes";
        }

        return "no";
    }
}
