package org.example.cp.oms.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.api.RequestProfile;
import org.example.cp.oms.spec.exception.OrderException;
import org.example.cp.oms.domain.model.vo.ProductDelegate;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.IProductDelegate;

import javax.validation.constraints.NotNull;

@Getter
@Slf4j
public class OrderModel implements IOrderModel {

    private String source;
    private String customerNo;

    private String orderNo;

    private String externalNo;

    private RequestProfile requestProfile;

    @Setter
    private String activity;

    @Setter
    private String step;

    private ProductDelegate productDelegate;

    @Getter
    private String x1, x2;

    public static OrderModel createWith(@NotNull OrderModelCreator creator) throws OrderException {
        log.debug("creating with {}", creator);
        return new OrderModel(creator).validate();
    }

    private OrderModel(OrderModelCreator creator) {
        this.source = creator.getSource();
        this.customerNo = creator.getCustomerNo();
        this.externalNo = creator.getExternalNo();
        this.requestProfile = creator.getRequestProfile();

        this.productDelegate = ProductDelegate.createWith(creator);
    }

    private OrderModel validate() throws OrderException {
        // 模型本身的基础校验
        return this;
    }

    @Override
    public void assignOrderNo(Object who, String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String currentStep() {
        return step;
    }

    @Override
    public String currentActivity() {
        return activity;
    }

    @Override
    public boolean isColdChain() {
        return false;
    }

    @Override
    public boolean isB2B() {
        return false;
    }

    @Override
    public void setX1(String x1) {
        this.x1 = x1;
    }

    @Override
    public void setX2(String x2) {
        this.x2 = x2;
    }

    @Override
    public IProductDelegate productDelegate() {
        return null;
    }

    @Override
    public RequestProfile requestProfile() {
        return requestProfile;
    }

    @Override
    public String customerProvidedOrderNo() {
        return externalNo;
    }

    public String label() {
        return "Order(source=" + source + ", customer=" + customerNo + ")";
    }
}
