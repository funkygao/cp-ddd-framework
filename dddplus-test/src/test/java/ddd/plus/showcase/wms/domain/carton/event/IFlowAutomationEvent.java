package ddd.plus.showcase.wms.domain.carton.event;

import java.io.Serializable;

public interface IFlowAutomationEvent extends Serializable {
    Integer TypeCaronFulfilled = 1;

    Integer getEventType();

}
