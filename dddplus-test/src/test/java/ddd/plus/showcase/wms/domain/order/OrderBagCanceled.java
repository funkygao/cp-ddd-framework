package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.BoundedDomainModel;
import lombok.experimental.Delegate;

public class OrderBagCanceled extends BoundedDomainModel<OrderBag> {
    protected OrderBagCanceled(OrderBag model) {
        super(model);
    }

    @Delegate
    OrderBag getModel() {
        return model;
    }

}
