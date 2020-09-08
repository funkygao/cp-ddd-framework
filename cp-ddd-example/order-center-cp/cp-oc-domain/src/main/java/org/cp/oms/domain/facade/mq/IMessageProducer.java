package org.cp.oms.domain.facade.mq;

import org.cp.oms.domain.model.OrderModel;

import javax.validation.constraints.NotNull;

public interface IMessageProducer {

    void produce(@NotNull OrderModel orderModel);
}
