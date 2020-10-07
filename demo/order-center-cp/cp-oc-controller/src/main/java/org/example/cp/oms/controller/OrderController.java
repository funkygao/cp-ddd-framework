package org.example.cp.oms.controller;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.api.RequestProfile;
import org.cdf.ddd.runtime.registry.Container;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderController {

    // DDD Application Layer depends on Domain Layer
    @Resource
    private SubmitOrder submitOrder;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "Hello cp-ddd-framework!";
    }

    // 下单服务
    @RequestMapping(value = "/order", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String submitOrder() {
        // DTO 转换为 domain model，通过creator保护、封装domain model
        // 具体项目使用MapStruct会更方便，这里为了演示，全手工进行对象转换了
        RequestProfile requestProfile = new RequestProfile();
        requestProfile.setTraceId(String.valueOf(System.nanoTime()));
        MDC.put("tid", requestProfile.getTraceId()); // session scope log identifier

        // 这里手工创建一个模拟下单的请求
        OrderModelCreator creator = new OrderModelCreator();
        creator.setRequestProfile(requestProfile);
        creator.setSource("ISV");
        creator.setCustomerNo("home");
        creator.setExternalNo("2020");
        OrderModel model = OrderModel.createWith(creator);

        // 调用domain service完成该use case
        submitOrder.submit(model);
        return "Order accepted!";
    }

    @RequestMapping(value = "/reloadIsv")
    @ResponseBody
    public String reloadIsv() {
        log.info("CWD:{}", System.getProperty("user.dir"));

        try {
            Container.getInstance().loadPartnerPlugin("isv", "order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar", true);
        } catch (Throwable cause) {
            log.error("fails to reload ISV Plugin Jar", cause);
            return cause.getMessage();
        }

        return "Reloaded!";
    }
}
