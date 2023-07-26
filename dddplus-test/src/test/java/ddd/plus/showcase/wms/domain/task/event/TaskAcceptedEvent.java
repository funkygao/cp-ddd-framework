package ddd.plus.showcase.wms.domain.task.event;

import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;
import io.github.dddplus.dsl.KeyEvent;
import lombok.Data;

import java.util.Map;

@Data
@KeyEvent
public class TaskAcceptedEvent implements IFlowAutomationEvent {
    private String taskNo;
    private String warehouseNo;
    private Map<String, Object> reserved;
}
