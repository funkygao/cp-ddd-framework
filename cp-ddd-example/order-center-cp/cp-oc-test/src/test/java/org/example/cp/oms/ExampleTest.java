package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class ExampleTest {

    @Resource
    private SubmitOrder submitOrder;

    // 演示的入口，展示如何把这个例子完整地跑起来
    @Test
    public void demoSubmitOrder() throws Throwable {
        // prepare the domain model
        OrderModelCreator creator = new OrderModelCreator();
        creator.setSource("ISV"); // IsvPartner
        creator.setCustomerNo("home"); // HomeAppliancePattern
        creator.setExternalNo("20200987655");
        OrderModel orderModel = OrderModel.createWith(creator);

        // 会触发 ISV的步骤编排：basic, persist, broadcast
        // 查看日志，了解具体执行情况
        submitOrder.submit(orderModel);
    }
}
