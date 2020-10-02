package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.UnderDevelopment;
import org.cdf.ddd.api.RequestProfile;
import org.cdf.ddd.runtime.registry.Container;
import org.cdf.ddd.runtime.registry.IPlugin;
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

import static org.example.cp.oms.LogAssert.assertContains;
import static org.junit.Assert.assertEquals;

@Slf4j
@Ignore
public class PluginMechanismTest {
    private URL remoteKaJar;
    private URL remoteIsvJar;

    private static final String localKaJar = "../../order-center-bp-ka/target/order-center-bp-ka-0.0.1.jar";
    private static final String localIsvJar = "../../order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar";

    @Before
    public void setUp() throws MalformedURLException {
        remoteIsvJar = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar?raw=true");
        remoteKaJar = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-ka-0.0.1.jar?raw=true");
    }

    @UnderDevelopment // 需要运行在 profile:plugin 下，运行前需要mvn package为Plugin打包
    @Test
    public void dynamicLoadPlugins() throws Throwable {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-test.xml");
        applicationContext.start();

        for (int i = 0; i < 2; i++) {
            // 同一个jar，load多次，模拟热更新，然后下单验证：走ISV前台逻辑
            log.info(String.join("", Collections.nCopies(50, String.valueOf(i + 1))));
            Container.getInstance().loadPartnerPlugin("isv", localIsvJar, true);
            submitOrder(applicationContext, true);

            // 通过日志验证执行正确性
            assertContains(
                    // 验证 AutoLoggerAspect 被创建
                    "Spring created instance AutoLoggerAspect!", "AutoLoggerAspect 注册 Spring lifecycle ok",
                    // IsvPartner在动态加载时自动创建实例
                    "ISV new instanced",
                    // isv.PluginListener 被调用
                    "ISV Jar loaded, ",
                    // @AutoLogger
                    "DecideStepsExt.decideSteps 入参",
                    // isv.DecideStepsExt.decideSteps 调用了中台的 stockService.preOccupyStock
                    "预占库存：SKU From ISV",
                    // ISV的步骤编排
                    "steps [basic, persist, mq]",
                    // @AutoLogger
                    "org.example.bp.oms.isv.extension.PresortExt.presort 入参",
                    // isv.PresortExt
                    "ISV里预分拣的结果：1", "count(a): 2", "仓库号：WH009",
                    // 加载properties资源，并且有中文
                    "加载资源文件成功！站点名称：北京市海淀区中关村中路1号",
                    // @AutoLogger
                    "org.example.bp.oms.isv.extension.CustomModel.explain 入参:",
                    // CustomModel，扩展属性机制
                    "站点联系人号码：139100988343，保存到x2字段",
                    "已经发送给MQ"
            );
        }

        log.info(String.join("", Collections.nCopies(50, "=")));

        // 加载KA插件，并给KA下单
        Container.getInstance().loadPartnerPlugin("ka", localKaJar, true);
        submitOrder(applicationContext, false);
        assertContains(
                "KA 预占库存 GSM098",
                "KA的锁TTL大一些",
                "steps [basic, persist]"
        );

        // 目前已经加载了2个Plugin Jar
        assertEquals(2, Container.getInstance().getActivePlugins().size());
        for (IPlugin plugin : Container.getInstance().getActivePlugins().values()) {
            log.info("Plugin: {}", plugin.getCode());
        }

        if (true) {
            return;
        }

        log.info("sleeping 2m，等待修改bp-isv里逻辑后发布新jar...");
        TimeUnit.MINUTES.sleep(2); // 等待手工发布新jar
        log.info("2m is up, go!");
        Container.getInstance().loadPartnerPlugin("isv", localIsvJar, true);
        submitOrder(applicationContext, true); // 重新提交订单，看看是否新jar逻辑生效

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
