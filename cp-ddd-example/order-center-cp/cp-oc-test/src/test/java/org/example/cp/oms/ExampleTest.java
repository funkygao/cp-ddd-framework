package org.example.cp.oms;

import org.cdf.ddd.api.RequestProfile;
import org.example.cp.oms.spec.exception.OrderException;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class ExampleTest {

    @Resource
    private SubmitOrder submitOrder;

    // 演示的入口，展示如何把这个例子完整地跑起来
    // 需要运行在 maven profile:demo 下
    @Test
    public void demoSubmitOrder() throws OrderException {
        // prepare the domain model
        RequestProfile requestProfile = new RequestProfile();
        // 演示个性化字段：站点联系人号码是ISV前台场景才会需要的字段，其他场景不需要
        requestProfile.getExt().put("_station_contact_", "139100988343");
        OrderModelCreator creator = new OrderModelCreator();
        creator.setSource("ISV"); // IsvPartner 会触发ISV前台相关的扩展点
        creator.setCustomerNo("home"); // HomeAppliancePattern 会触发家电相关的扩展点
        creator.setExternalNo("20200987655");
        creator.setRequestProfile(requestProfile);
        OrderModel orderModel = OrderModel.createWith(creator);

        // ISV业务前台的下单执行：
        //   SerializableIsolationExt -> DecideStepsExt -> BasicStep(PresortExt) -> PersistStep(AssignOrderNoExt, CustomModelAbility) -> BroadcastStep
        // 查看日志，了解具体执行情况
        submitOrder.submit(orderModel);

        // 下面是验证失败的场景：ISV前台入参里没有传递必填的“站点联系人号码”
        requestProfile.getExt().clear();
        orderModel = OrderModel.createWith(creator);
        try {
            submitOrder.submit(orderModel);
            fail();
        } catch (OrderException expected) {
            assertEquals("109", expected.getCode());
        }

    }
}
