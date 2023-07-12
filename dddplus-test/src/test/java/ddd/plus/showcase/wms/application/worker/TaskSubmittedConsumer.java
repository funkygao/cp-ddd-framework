package ddd.plus.showcase.wms.application.worker;

import com.google.gson.Gson;
import ddd.plus.showcase.wms.application.service.CheckingAppService;
import ddd.plus.showcase.wms.application.worker.dto.SubmitTaskDto;
import io.github.dddplus.dsl.KeyUsecase;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 提交复核任务的MQ消费者.
 */
@Component
public class TaskSubmittedConsumer {
    private static final Gson gson = new Gson();

    @Resource
    private CheckingAppService checkingAppService;

    @KeyUsecase(name = "消费接收复核任务", in = "json")
    public void onMessage(String payload) {
        SubmitTaskDto dto = gson.fromJson(payload, SubmitTaskDto.class);
        checkingAppService.submitTask(dto);
    }
}
