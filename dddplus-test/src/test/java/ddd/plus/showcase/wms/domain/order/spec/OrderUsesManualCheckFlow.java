package ddd.plus.showcase.wms.domain.order.spec;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.buddy.specification.AbstractSpecification;
import io.github.dddplus.buddy.specification.Notification;

public class OrderUsesManualCheckFlow extends AbstractSpecification<Order> {
    @Override
    public boolean isSatisfiedBy(Order candidate, Notification notification) {
        return candidate.getConstraint().manualCheck();
    }
}
