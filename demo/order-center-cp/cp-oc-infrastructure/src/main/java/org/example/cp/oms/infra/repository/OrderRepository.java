package org.example.cp.oms.infra.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.facade.repository.IOrderRepository;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.infra.manager.IOrderManager;
import org.example.cp.oms.infra.po.OrderMainData;
import org.example.cp.oms.infra.translator.OrderTranslator;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Repository
@Slf4j
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOrderManager orderManager;

    @Override
    public void persist(@NotNull OrderModel orderModel) {
        log.info("落库：{}", orderModel);

        if (true) {
            return;
        }

        OrderMainData orderMainData = OrderTranslator.instance.translate(orderModel);
        orderManager.insert(orderMainData);
    }
}
