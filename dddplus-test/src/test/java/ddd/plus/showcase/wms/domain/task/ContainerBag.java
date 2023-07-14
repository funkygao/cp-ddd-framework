package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.ListBag;
import io.github.dddplus.model.encapsulation.AllowedAccessors;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@KeyRelation(whom = Container.class, type = KeyRelation.Type.HasMany)
public class ContainerBag extends ListBag<Container> {
    protected ContainerBag(List<Container> items) {
        super(items);
    }

    public static ContainerBag of(@NonNull List<Container> containers) {
        return new ContainerBag(containers);
    }

    /**
     * 该容器的总商品种类(品数).
     */
    @KeyRule
    int totalSku() {
        int total = 0;
        for (Container container : items) {
            total += container.getContainerItemBag().totalSku();
        }
        return total;
    }

    /**
     * 该容器的总要货量.
     */
    @KeyRule
    BigDecimal totalQty() {
        return items.stream()
                .map(Container::getContainerItemBag)
                .map(ContainerItemBag::totalQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @KeyRule
    BigDecimal totalPendingQty() {
        return items.stream()
                .map(Container::getContainerItemBag)
                .map(ContainerItemBag::totalPendingQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ContainerItemBagPending pendingItemBag() {
        List<ContainerItem> containerItems = new ArrayList<>();
        for (Container container : items) {
            containerItems.addAll(container.getContainerItemBag().pendingItems());
        }
        return new ContainerItemBagPending(ContainerItemBag.of(containerItems));
    }

    void remove(Set<OrderLineNo> orderLineNos) {
        // TBD
    }

    @KeyBehavior
    ContainerItemBag confirmQty(BigDecimal qty) {
        return pendingItemBag().confirmQty(qty);
    }

    /**
     * 所有的出库单号.
     */
    @KeyRule
    Set<OrderNo> orderNoSet() {
        return items.stream()
                .flatMap(container -> container.getContainerItemBag().orderNoSet().stream())
                .collect(Collectors.toSet());
    }

    void enrichSkuInfo(List<Sku> skus) {
        // 挂到各个 ContainerItem group by skuNo
    }

    @AllowedAccessors(ITaskRepository.class)
    public List<ContainerItem> flatItems() {
        return null;
    }
}
