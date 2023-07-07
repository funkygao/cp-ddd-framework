package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;

public class SomeEventHandler extends AbstractEventHandler {
    @Override
    public void processMyEvent(IFlowAutomationEvent event) {

    }

    @Override
    protected boolean isMine(IFlowAutomationEvent event) {
        return false;
    }
}
