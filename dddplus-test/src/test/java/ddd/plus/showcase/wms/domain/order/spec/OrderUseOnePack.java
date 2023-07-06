package ddd.plus.showcase.wms.domain.order.spec;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;

public class OrderUseOnePack extends AbstractSpecification<Order> {
    @Override
    public boolean isSatisfiedBy(Order order, Notification notification) {
        return order.constraint().isUseOnePack();
    }
}
