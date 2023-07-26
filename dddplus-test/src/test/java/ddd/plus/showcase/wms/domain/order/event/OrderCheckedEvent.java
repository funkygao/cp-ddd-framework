package ddd.plus.showcase.wms.domain.order.event;

import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;
import io.github.dddplus.dsl.KeyEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@KeyEvent
@AllArgsConstructor
@Getter
public class OrderCheckedEvent implements IFlowAutomationEvent {
    private String orderNo;
    private String warehouseNo;
}
