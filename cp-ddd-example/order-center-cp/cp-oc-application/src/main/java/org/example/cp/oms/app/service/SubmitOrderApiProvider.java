package org.example.cp.oms.app.service;

import org.cdf.ddd.api.RequestProfile;
import org.example.cp.oms.client.api.SubmitOrderApi;
import org.example.cp.oms.client.dto.SubmitOrderRequest;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.model.OrderModelCreator;
import org.example.cp.oms.domain.service.SubmitOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Service
public class SubmitOrderApiProvider implements SubmitOrderApi {

    @Resource
    private SubmitOrder submitOrder; // domain service

    @Override
    public void submit(@NotNull RequestProfile requestProfile, @NotNull SubmitOrderRequest submitOrderRequest) {
        // DTO 转换为 domain model，通过creator保护、封装domain model
        // 具体项目使用MapStruct会更方便，这里为了演示，全手工进行对象转换了
        OrderModelCreator creator = new OrderModelCreator();
        creator.setRequestProfile(requestProfile);
        creator.setSource(submitOrderRequest.getSource());
        creator.setCustomerNo(submitOrderRequest.getCustomerNo());
        creator.setExternalNo(submitOrderRequest.getExternalNo());
        OrderModel model = OrderModel.createWith(creator);

        // 调用domain service完成该use case
        submitOrder.submit(model);
    }
}
