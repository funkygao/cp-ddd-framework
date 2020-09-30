package org.example.cp.oms.infra.mq;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.facade.mq.IMessageProducer;
import org.example.cp.oms.domain.model.OrderModel;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Slf4j
public class MessageProducer implements IMessageProducer {

    @Override
    public void produce(@NotNull OrderModel orderModel) {
        log.info("已经发送给MQ：{}", orderModel);
    }
}
