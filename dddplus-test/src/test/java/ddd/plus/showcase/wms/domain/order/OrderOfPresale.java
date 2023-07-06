package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.BoundedDomainModel;

/**
 * 出库单的预售场景.
 */
@KeyRelation(whom = Order.class, type = KeyRelation.Type.Contextual)
public class OrderOfPresale extends BoundedDomainModel<Order> {
    OrderOfPresale(Order order) {
        this.model = order;
    }

    /**
     * 尾款已结清?
     */
    @KeyRule
    public boolean isFinalPaid() {
        return true;
    }
}
