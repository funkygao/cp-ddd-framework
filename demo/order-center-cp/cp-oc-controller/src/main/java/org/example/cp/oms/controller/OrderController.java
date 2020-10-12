package org.example.cp.oms.controller;

import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.api.RequestProfile;
import io.github.dddplus.runtime.registry.Container;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderController {

    // DDD Application Layer depends on Domain Layer
    @Resource
    private SubmitOrder submitOrder;

    // 下单服务
    @RequestMapping(value = "/order", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String submitOrder(@RequestParam(required = false) String type) {
        if (type == null) {
            type = "ISV"; // ISV by default
        }

        log.info("type={}", type);

        // DTO 转换为 domain model，通过creator保护、封装domain model
        // 具体项目使用MapStruct会更方便，这里为了演示，全手工进行对象转换了
        RequestProfile requestProfile = new RequestProfile();
        requestProfile.setTraceId(String.valueOf(System.nanoTime()));
        // 演示个性化字段：站点联系人号码是ISV前台场景才会需要的字段，其他场景不需要
        requestProfile.getExt().put("_station_contact_", "139100988343");
        MDC.put("tid", requestProfile.getTraceId()); // session scope log identifier

        // 这里手工创建一个模拟下单的请求
        OrderModelCreator creator = new OrderModelCreator();
        creator.setRequestProfile(requestProfile);
        creator.setSource(type);
        creator.setCustomerNo("home");
        creator.setExternalNo("20200987655");
        OrderModel model = OrderModel.createWith(creator);

        // 调用domain service完成该use case
        submitOrder.submit(model);
        // ISV业务前台的下单执行：
        //   SerializableIsolationExt -> DecideStepsExt -> BasicStep(PresortExt) -> PersistStep(AssignOrderNoExt, CustomModelAbility) -> BroadcastStep
        // 查看日志，了解具体执行情况
        return "Order accepted!";
    }

    @RequestMapping(value = "/reloadIsv")
    @ResponseBody
    public String reloadIsv() {
        MDC.put("tid", String.valueOf(System.nanoTime()));

        log.info("CWD:{}", System.getProperty("user.dir"));

        try {
            Container.getInstance().loadPartnerPlugin("isv", "order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar", true);
        } catch (Throwable cause) {
            log.error("fails to reload ISV Plugin Jar", cause);
            return cause.getMessage();
        }

        return "ISV Plugin Reloaded!";
    }
}
