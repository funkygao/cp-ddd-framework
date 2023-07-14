package ddd.plus.showcase.wms.infrastructure.publisher;

import com.google.gson.Gson;
import ddd.plus.showcase.wms.domain.common.publisher.IEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PulsarProducer implements IEventPublisher {
    private static final Gson gson = new Gson();

    @Override
    public void publish(Object event) {
        gson.toJson(event);
    }
}
