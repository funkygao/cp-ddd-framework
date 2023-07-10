package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.BoundedDomainModel;

import java.util.Set;

/**
 * 被客户取消的订单集合.
 */
public class OrderBagCanceled extends BoundedDomainModel<OrderBag> {

    OrderBagCanceled(OrderBag bag) {
        this.model = bag;
    }

    public Set<OrderLineNo> orderLineNos() {
        return unbounded().orderLineNos();
    }

}
