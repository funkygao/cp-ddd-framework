package ddd.plus.showcase.wms.domain.order.spec;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;

public class OrderUsesManualCheckFlow extends AbstractSpecification<Order> {
    @Override
    public boolean isSatisfiedBy(Order candidate, Notification notification) {
        return candidate.constraint().allowManualCheck();
    }
}
