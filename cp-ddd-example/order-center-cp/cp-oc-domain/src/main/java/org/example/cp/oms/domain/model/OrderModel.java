package org.example.cp.oms.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.exception.OrderException;
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

    private String externNo;

    @Setter
    private String activity;

    @Setter
    private String step;

    private ProductDelegate productDelegate;

    public static OrderModel createWith(@NotNull OrderModelCreator creator) throws OrderException {
        log.debug("creating with {}", creator);
        return new OrderModel(creator).validate();
    }

    private OrderModel(OrderModelCreator creator) {
        this.source = creator.getSource();
        this.customerNo = creator.getCustomerNo();
        this.externNo = creator.getExternalNo();

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
    public IProductDelegate productDelegate() {
        return null;
    }

    @Override
    public String customerProvidedOrderNo() {
        return externNo;
    }

    public String label() {
        return "Order(source=" + source + ", customer=" + customerNo + ")";
    }
}
