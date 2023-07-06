package ddd.plus.showcase.wms.app.worker;

import com.google.gson.Gson;
import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;
import io.github.dddplus.dsl.KeyUsecase;
import org.springframework.stereotype.Component;

/**
 * 作业的流程自动化.
 */
@Component
public class FlowAutoExecutionConsumer {
    private static final Gson gson = new Gson();

    @KeyUsecase
    public void onMessage(String payload) {
        IFlowAutomationEvent event = gson.fromJson(payload, IFlowAutomationEvent.class);
        event.getEventType();
    }
}
