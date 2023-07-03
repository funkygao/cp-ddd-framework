package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.base.ListBag;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@KeyRelation(whom = Container.class, type = KeyRelation.Type.HasMany)
public class ContainerBag extends ListBag<Container> {

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
        BigDecimal total = BigDecimal.ZERO;
        for (Container container : items) {
            total = total.add(container.totalQty());
        }
        return total;
    }

    @KeyRule
    ContainerItemBagPending pendingItemBag() {
        List<ContainerItem> containerItems = new ArrayList<>();
        for (Container container : items) {
            containerItems.addAll(container.pendingBag().items());
        }
        return new ContainerItemBagPending(ContainerItemBag.of(containerItems));
    }

    @KeyRule
    public Set<OrderNo> orderNoSet() {
        Set<OrderNo> orderNos = new HashSet<>(size());
        for (Container container : items) {
            orderNos.addAll(container.orderNoSet());
        }
        return orderNos;
    }
}
