package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;

public abstract class AbstractEventHandler<Event extends IFlowAutomationEvent> {
    protected AbstractEventHandler successor;

    protected final void triggerSuccessor(IFlowAutomationEvent event) {
        if (successor != null) {
            successor.processEvent(event);
        }
    }

    public void setSuccessor(AbstractEventHandler successor) {
        this.successor = successor;
    }

    public final void processEvent(IFlowAutomationEvent event) {
        if (!isMine(event)) {
            if (successor != null) {
                // 交给后续步骤处理
                successor.processEvent(event);
            }
            return;
        }

        // 属于我的事件，处理它：何时停下来，由本步骤决定
        processMyEvent((Event) event);
    }

    protected abstract void processMyEvent(Event event);

    protected abstract boolean isMine(IFlowAutomationEvent event);
}
