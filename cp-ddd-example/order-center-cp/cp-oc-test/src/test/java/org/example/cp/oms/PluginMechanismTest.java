package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.UnderDevelopment;
import org.cdf.ddd.api.RequestProfile;
import org.cdf.ddd.runtime.registry.Container;
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

    @UnderDevelopment // 需要运行在 profile:plugin 下，运行前需要mvn package为Plugin打包
    @Test
    public void dynamicLoadPlugins() throws Throwable {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-test.xml");
        applicationContext.start();

        Container.getInstance().loadPartnerPlugin(localIsvJar, "org.example.bp");
        submitOrder(applicationContext);
        if (true) {
            return;
        }

        for (int i = 0; i < 2; i++) {
            // 同一个jar，load多次
            log.info("n={}", i + 1);
            Container.getInstance().loadPartnerPlugin(remoteIsvJar, "org.example.bp");
        }

        Container.getInstance().loadPartnerPlugin(remoteKaJar, "org.example.bp");

        Container.getInstance().loadPatternPlugin(remotePatternJar, "org.example.cp");

        Container.getInstance().unloadPatternPlugin("hair");

        if (true) {
            log.info("sleeping 2m，等待修改bp-isv里逻辑后发布新jar...");
            TimeUnit.MINUTES.sleep(2); // 等待手工发布新jar
            log.info("2m is up, go!");
            Container.getInstance().loadPartnerPlugin(remoteIsvJar, "org.example.bp");
            submitOrder(applicationContext); // 重新提交订单，看看是否新jar逻辑生效
        }

        // 去掉ISV Partner，再提交订单，接单步骤会变空的
        Container.getInstance().unloadPartnerPlugin("ISV");
        submitOrder(applicationContext);

        applicationContext.stop();
    }

    private void submitOrder(ApplicationContext applicationContext) {
        // prepare the domain model
        RequestProfile requestProfile = new RequestProfile();
        requestProfile.getExt().put("_station_contact_", "139100988343");
        OrderModelCreator creator = new OrderModelCreator();
        creator.setRequestProfile(requestProfile);
        creator.setSource("ISV"); // IsvPartner
        creator.setCustomerNo("home"); // HomeAppliancePattern
        creator.setExternalNo("20200987655");
        OrderModel orderModel = OrderModel.createWith(creator);

        // call the domain service
        SubmitOrder submitOrder = (SubmitOrder) applicationContext.getBean("submitOrder");
        // Partner(ISV)的下单执行：
        //     SerializableIsolationExt -> DecideStepsExt -> BasicStep(PresortExt) -> PersistStep(AssignOrderNoExt) -> BroadcastStep
        // Partner(KA)的下单执行：
        //     SerializableIsolationExt -> DecideStepsExt -> BasicStep -> PersistStep(AssignOrderNoExt)
        submitOrder.submit(orderModel);
    }
}
