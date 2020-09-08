package org.example.cp.oms.infra.repository;

import org.example.cp.oms.domain.facade.repository.IOrderRepository;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.infra.manager.IOrderManager;
import org.example.cp.oms.infra.po.OrderMainData;
import org.example.cp.oms.infra.translator.OrderTranslator;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOrderManager orderManager;

    @Override
    public void persist(@NotNull OrderModel orderModel) {
        OrderMainData orderMainData = OrderTranslator.instance.translate(orderModel);
        orderManager.insert(orderMainData);
    }
}
