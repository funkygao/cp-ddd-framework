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
import java.util.Collections;
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

        log.info(String.join("", Collections.nCopies(50, "*")));

        // 目前的问题：第二次循环时抛出异常
        // org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.example.bp.oms.isv.IsvPartner' available
        for (int i = 0; i < 1; i++) {
            // 同一个jar，load多次，模拟热更新
            log.info("n={}", i + 1);
            Container.getInstance().loadPartnerPlugin(localIsvJar, true);
            submitOrder(applicationContext, true);
            log.info(String.join("", Collections.nCopies(50, "=")));
        }

        Container.getInstance().loadPartnerPlugin(localKaJar, true);

        if (true) {
            return;
        }

        Container.getInstance().loadPartnerPlugin(remoteKaJar, true);

        Container.getInstance().loadPatternPlugin(remotePatternJar, true);

        Container.getInstance().unloadPatternPlugin("hair");

        if (true) {
            log.info("sleeping 2m，等待修改bp-isv里逻辑后发布新jar...");
            TimeUnit.MINUTES.sleep(2); // 等待手工发布新jar
            log.info("2m is up, go!");
            Container.getInstance().loadPartnerPlugin(remoteIsvJar, true);
            submitOrder(applicationContext, true); // 重新提交订单，看看是否新jar逻辑生效
        }

        // 去掉ISV Partner，再提交订单，接单步骤会变空的
        Container.getInstance().unloadPartnerPlugin("ISV");
        submitOrder(applicationContext, false);

        applicationContext.stop();
    }

    private void submitOrder(ApplicationContext applicationContext, boolean useIsvPartner) {
        // prepare the domain model
        RequestProfile requestProfile = new RequestProfile();
        requestProfile.getExt().put("_station_contact_", "139100988343");
        OrderModelCreator creator = new OrderModelCreator();
        creator.setRequestProfile(requestProfile);
        if (useIsvPartner) {
            creator.setSource("ISV"); // IsvPartner
        } else {
            creator.setSource("KA"); // KaPartner
        }
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
