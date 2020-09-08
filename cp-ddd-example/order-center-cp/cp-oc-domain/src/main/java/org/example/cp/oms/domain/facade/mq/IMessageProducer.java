package org.example.cp.oms.domain.facade.mq;

import org.example.cp.oms.domain.model.OrderModel;

import javax.validation.constraints.NotNull;

public interface IMessageProducer {

    void produce(@NotNull OrderModel orderModel);
}
