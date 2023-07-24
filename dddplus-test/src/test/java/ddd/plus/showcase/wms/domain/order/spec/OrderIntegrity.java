package ddd.plus.showcase.wms.domain.order.spec;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;

/**
 * 订单完整性规约.
 */
public class OrderIntegrity extends AbstractSpecification<Order> {
    @Override
    public boolean isSatisfiedBy(Order order, Notification notification) {
        // 订单的货品可能分散在不同拣货区，经过合流、不同复核台，此时它们凑齐了吗？
        return true;
    }
}
