package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.runtime.DDD;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
@Ignore
public class ExampleTest {

    @Test
    public void dynamicLoadPlugins() throws Throwable {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-test.xml");
        applicationContext.start();

        for (int i = 0; i < 2; i++) {
            // 同一个jar，load多次
            log.info("N:{}", i);
            DDD.getContainer().loadPartnerPlugin(new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar?raw=true"), "org.example.bp");
        }

        DDD.getContainer().loadPartnerPlugin(new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-ka-0.0.1.jar?raw=true"), "org.example.bp");

        DDD.getContainer().loadPatternPlugin(new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-pattern-0.0.1.jar?raw=true"), "org.example.cp");

        DDD.getContainer().unloadPatternPlugin("hair");

        // prepare the domain model
        OrderModelCreator creator = new OrderModelCreator();
        creator.setSource("ISV"); // IsvPartner
        creator.setCustomerNo("home"); // HomeAppliancePattern
        creator.setExternalNo("20200987655");
        OrderModel orderModel = OrderModel.createWith(creator);

        // call the domain service
        SubmitOrder submitOrder = (SubmitOrder) applicationContext.getBean("submitOrder");
        // 会触发 ISV的步骤编排：basic, persist, broadcast
        // 相关的Pattern：IPresortExt
        submitOrder.submit(orderModel);

        if (true) {
            log.info("sleeping 2m，等待修改bp-isv里逻辑后发布新jar...");
            TimeUnit.MINUTES.sleep(2); // 等待手工发布新jar
            log.info("2m is up, go!");
            DDD.getContainer().loadPartnerPlugin(new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar?raw=true"), "org.example.bp");
            submitOrder.submit(orderModel); // 重新提交订单，看看是否新jar逻辑生效
        }

        // 去掉ISV Partner，再提交订单，接单步骤会变空的
        DDD.getContainer().unloadPartnerPlugin("ISV");
        submitOrder.submit(orderModel);

        applicationContext.stop();
    }
}
