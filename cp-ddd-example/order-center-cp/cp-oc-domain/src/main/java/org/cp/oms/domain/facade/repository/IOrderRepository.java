package org.cp.oms.domain.facade.repository;

import org.cp.oms.domain.model.OrderModel;

import javax.validation.constraints.NotNull;

public interface IOrderRepository {

    void persist(@NotNull OrderModel orderModel);
}
