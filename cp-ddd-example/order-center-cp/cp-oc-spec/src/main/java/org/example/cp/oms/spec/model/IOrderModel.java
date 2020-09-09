package org.example.cp.oms.spec.model;

import org.example.cp.oms.spec.model.vo.IProductDelegate;
import org.ddd.cp.ddd.model.IDomainModel;
import lombok.NonNull;

/**
 * 中台为业务前台输出的领域模型：以接口的方式控制visibility.
 */
public interface IOrderModel extends IDomainModel {

    /**
     * 获取订单里包含的产品信息.
     */
    IProductDelegate productDelegate();

    /**
     * 获取订单来源.
     */
    String getSource();

    /**
     * 获取客户编号.
     */
    String getCustomerNo();

    /**
     * 客户提供的订单号.
     */
    String customerProvidedOrderNo();

    /**
     * 分配订单号.
     *
     * @param who
     * @param orderNo
     */
    void assignOrderNo(@NonNull Object who, String orderNo);

    String currentStep();

    String currentActivity();

    /**
     * 是否冷链业务.
     */
    boolean isColdChain();

    boolean isB2B();
}
