package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.runtime.DDD;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
@Ignore
public class PluginMechanismTest {
    private URL remoteKaJar;
    private URL remoteIsvJar;
    private URL remotePatternJar;

    private static final String localKaJar = "../../order-center-bp-ka/target/order-center-bp-ka-0.0.1.jar";
    private static final String localIsvJar = "../../order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar";

    @Before
    public void setUp() throws MalformedURLException {
        remoteIsvJar = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar?raw=true");
        remoteKaJar = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-ka-0.0.1.jar?raw=true");
        remotePatternJar = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-pattern-0.0.1.jar?raw=true");
    }

    @Test
    public void dynamicLoadPlugins() throws Throwable {
        log.info("CWD: {}", System.getProperty("user.dir")); // cp-ddd-framework/cp-ddd-example/order-center-cp/cp-oc-test

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-test.xml");
        applicationContext.start();

        DDD.getContainer().loadPartnerPlugin(localIsvJar, "org.example.bp");
        submitOrder(applicationContext);
        if (true) {
            return;
        }

        for (int i = 0; i < 2; i++) {
            // 同一个jar，load多次
            log.info("n={}", i + 1);
            DDD.getContainer().loadPartnerPlugin(remoteIsvJar, "org.example.bp");
        }

        DDD.getContainer().loadPartnerPlugin(remoteKaJar, "org.example.bp");

        DDD.getContainer().loadPatternPlugin(remotePatternJar, "org.example.cp");

        DDD.getContainer().unloadPatternPlugin("hair");

        if (true) {
            log.info("sleeping 2m，等待修改bp-isv里逻辑后发布新jar...");
            TimeUnit.MINUTES.sleep(2); // 等待手工发布新jar
            log.info("2m is up, go!");
            DDD.getContainer().loadPartnerPlugin(remoteIsvJar, "org.example.bp");
            submitOrder(applicationContext); // 重新提交订单，看看是否新jar逻辑生效
        }

        // 去掉ISV Partner，再提交订单，接单步骤会变空的
        DDD.getContainer().unloadPartnerPlugin("ISV");
        submitOrder(applicationContext);

        applicationContext.stop();
    }

    private void submitOrder(ApplicationContext applicationContext) {
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
    }
}
