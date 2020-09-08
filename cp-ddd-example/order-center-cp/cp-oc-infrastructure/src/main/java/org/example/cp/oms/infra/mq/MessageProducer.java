package org.example.cp.oms.infra.mq;

import org.example.cp.oms.domain.facade.mq.IMessageProducer;
import org.example.cp.oms.domain.model.OrderModel;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class MessageProducer implements IMessageProducer {

    @Override
    public void produce(@NotNull OrderModel orderModel) {

    }
}
