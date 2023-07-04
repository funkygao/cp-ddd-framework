package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.ListBag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@KeyRelation(whom = Container.class, type = KeyRelation.Type.HasMany)
public class ContainerBag extends ListBag<Container> {

    protected ContainerBag(List<Container> items) {
        super(items);
    }

    /**
     * 该容器的总商品种类(品数).
     */
    @KeyRule
    public int totalSku() {
        int total = 0;
        for (Container container : items) {
            total += container.totalSku();
        }
        return total;
    }

    /**
     * 该容器的总要货量.
     */
    @KeyRule
    public BigDecimal totalQty() {
        return items.stream().map(Container::totalQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @KeyRule
    public BigDecimal totalPendingQty() {
        return items.stream().map(Container::totalPendingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 待复核的所有容器明细.
     */
    @KeyRule
    ContainerItemBagPending pendingItemBag() {
        List<ContainerItem> containerItems = new ArrayList<>();
        for (Container container : items) {
            containerItems.addAll(container.pendingBag().items());
        }
        return new ContainerItemBagPending(ContainerItemBag.of(containerItems));
    }

    /**
     * 所有的出库单号.
     */
    @KeyRule
    public Set<OrderNo> orderNoSet() {
        Set<OrderNo> orderNos = new HashSet<>(size());
        for (Container container : items) {
            orderNos.addAll(container.orderNoSet());
        }
        return orderNos;
    }
}
