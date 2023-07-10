package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.BoundedDomainModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 待复核的货品集合.
 *
 * <p>它们可能属于不同的订单.</p>
 */
class ContainerItemBagPending extends BoundedDomainModel<ContainerItemBag> {

    ContainerItemBagPending(ContainerItemBag bag) {
        this.model = bag;
    }

    ContainerItemBag confirmQty(BigDecimal qty) {
        return model.confirmQty(qty);
    }

    List<ContainerItem> items() {
        return model.items();
    }

}
