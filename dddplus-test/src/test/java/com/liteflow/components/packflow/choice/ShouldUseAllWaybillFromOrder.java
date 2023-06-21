package com.liteflow.components.packflow.choice;

import com.liteflow.components.packflow.context.PackFlowContext;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeSwitchComponent;

@LiteflowComponent("ShouldUseAllWaybillFromOrder")
public class ShouldUseAllWaybillFromOrder extends NodeSwitchComponent {
    @Override
    public String processSwitch() throws Exception {
        PackFlowContext context = this.getFirstContextBean();
        if (context.getSo().useWaybillNoFromOrder()) {
            return "yes";
        }

        return "no";
    }
}
