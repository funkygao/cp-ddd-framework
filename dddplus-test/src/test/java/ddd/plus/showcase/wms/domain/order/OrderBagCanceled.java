package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.BoundedDomainModel;

public class OrderBagCanceled extends BoundedDomainModel<OrderBag> {

    OrderBagCanceled(OrderBag bag) {
        this.model = bag;
    }

}
