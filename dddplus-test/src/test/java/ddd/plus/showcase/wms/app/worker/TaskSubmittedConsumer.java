package ddd.plus.showcase.wms.app.worker;

import io.github.dddplus.dsl.KeyUsecase;
import org.springframework.stereotype.Component;

/**
 * 提交复核任务的MQ消费者.
 */
@Component
public class TaskSubmittedConsumer {

    @KeyUsecase(name = "消费接收复核任务", in = "json")
    public void onMessage(String payload) {

    }
}
