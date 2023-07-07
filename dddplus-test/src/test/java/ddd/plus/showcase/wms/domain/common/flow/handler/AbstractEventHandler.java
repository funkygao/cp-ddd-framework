package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;

public abstract class AbstractEventHandler {
    protected AbstractEventHandler successor;

    public void setSuccessor(AbstractEventHandler successor) {
        this.successor = successor;
    }

    public final void processEvent(IFlowAutomationEvent event) {
        if (!isMine(event)) {
            if (successor != null) {
                successor.processEvent(event);
            }
            return;
        }

        processMyEvent(event);
    }

    protected abstract void processMyEvent(IFlowAutomationEvent event);

    protected abstract boolean isMine(IFlowAutomationEvent event);
}
