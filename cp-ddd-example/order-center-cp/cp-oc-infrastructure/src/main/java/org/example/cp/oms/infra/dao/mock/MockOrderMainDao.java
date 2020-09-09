package org.example.cp.oms.infra.dao.mock;

import org.example.cp.oms.infra.dao.OrderMainDao;
import org.example.cp.oms.infra.po.OrderMainData;
import org.springframework.stereotype.Component;

@Component
public class MockOrderMainDao implements OrderMainDao {
    @Override
    public void insert(OrderMainData orderMainData) {

    }
}
