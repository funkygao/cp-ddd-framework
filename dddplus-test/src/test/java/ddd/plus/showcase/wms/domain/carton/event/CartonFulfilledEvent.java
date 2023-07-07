package ddd.plus.showcase.wms.domain.carton.event;

import io.github.dddplus.dsl.KeyEvent;
import lombok.Data;

import java.util.Map;

@Data
@KeyEvent(type = KeyEvent.Type.Local)
public class CartonFulfilledEvent implements IFlowAutomationEvent {
    private String cartonNo;
    private String warehouseNo;
    private Map<String, Object> reserved;

    @Override
    public Integer getEventType() {
        return TypeCartonFulfilled;
    }
}
