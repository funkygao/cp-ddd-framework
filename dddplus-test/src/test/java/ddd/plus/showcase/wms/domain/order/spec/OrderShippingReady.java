package ddd.plus.showcase.wms.domain.order.spec;

import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.dict.ProductionStatus;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;

public class OrderShippingReady extends AbstractSpecification<Order> {
    @Override
    public boolean isSatisfiedBy(Order order, Notification notification) {
        return order.getProductionStatus().equals(ProductionStatus.Cartonized);
    }
}
