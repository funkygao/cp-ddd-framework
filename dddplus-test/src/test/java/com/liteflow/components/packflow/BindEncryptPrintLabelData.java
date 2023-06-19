package com.liteflow.components.packflow;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent("BindEncryptPrintLabelData")
public class BindEncryptPrintLabelData extends NodeComponent {

    // 普通组件，用于THEN/WHEN
    @Override
    public void process() throws Exception {
        // 可以获取Spring bean
        BindWaybill bindWaybill = this.getContextBean(BindWaybill.class);

        this.getChainId();
        this.getNodeId();
        this.getRequestData();
    }


    @Override
    public boolean isAccess() {
        return super.isAccess();
    }

    @Override
    public boolean isContinueOnError() {
        return super.isContinueOnError();
    }
}
