package org.example.cp.oms.infra.manager.impl;

import org.example.cp.oms.infra.dao.OrderMainDao;
import org.example.cp.oms.infra.manager.IOrderManager;
import org.example.cp.oms.infra.po.OrderMainData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class OrderManager implements IOrderManager {

    @Resource
    private OrderMainDao orderMainDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(OrderMainData orderMainData) {
        orderMainDao.insert(orderMainData);
    }
}
