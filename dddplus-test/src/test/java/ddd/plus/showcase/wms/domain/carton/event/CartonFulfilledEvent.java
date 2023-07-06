package ddd.plus.showcase.wms.domain.carton.event;

import io.github.dddplus.dsl.KeyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@KeyEvent(type = KeyEvent.Type.Local)
public class CartonFulfilledEvent implements IFlowAutomationEvent {
    private Integer eventType = TypeCaronFulfilled;
    private String cartonNo;

    private Map<String, Object> reserved;
}
