package ddd.plus.showcase.wms.domain.carton.event;

import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;
import io.github.dddplus.dsl.KeyEvent;
import lombok.Data;

import java.util.Map;

@Data
@KeyEvent
public class CartonFulfilledEvent implements IFlowAutomationEvent {
    private String cartonNo;
    private String warehouseNo;
    private Map<String, Object> reserved;
}
