package ddd.plus.showcase.wms.domain.order.event;

import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;
import io.github.dddplus.dsl.KeyEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@KeyEvent(type = KeyEvent.Type.Local)
@AllArgsConstructor
@Getter
public class OrderCheckedEvent implements IFlowAutomationEvent {
    private String orderNo;
    private String warehouseNo;
}
