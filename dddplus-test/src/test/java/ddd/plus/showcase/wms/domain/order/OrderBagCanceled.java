package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.BoundedDomainModel;
import lombok.experimental.Delegate;

public class OrderBagCanceled extends BoundedDomainModel<OrderBag> {

    OrderBagCanceled(OrderBag bag) {
        this.model = bag;
    }

    @Delegate
    OrderBag getModel() {
        return model;
    }

}
