package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@KeyRelation(whom = ContainerItem.class, type = KeyRelation.Type.HasMany)
public class ContainerItemBag extends ListBag<ContainerItem> implements IUnboundedDomainModel {
    protected ContainerItemBag(List<ContainerItem> items) {
        super(items);
    }

    public static ContainerItemBag of(@NonNull List<ContainerItem> items) {
        return new ContainerItemBag(items);
    }

    /**
     * 该容器的总商品种类(品数).
     */
    @KeyRule
    int totalSku() {
        return items.size();
    }

    /**
     * 该容器的总货量.
     */
    @KeyRule
    BigDecimal totalQty() {
        return items.stream().map(ContainerItem::getExpectedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @KeyRule
    BigDecimal totalPendingQty() {
        return items.stream().map(ContainerItem::getPendingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    Set<OrderNo> orderNoSet() {
        Set<OrderNo> orderNos = new HashSet<>(size());
        for (ContainerItem item : items) {
            orderNos.add(item.getOrderLineNo().getOrderNo());
        }
        return orderNos;
    }

    List<ContainerItem> pendingItems() {
        return pendingItemBag().items();
    }

    private ContainerItemBagPending pendingItemBag() {
        return new ContainerItemBagPending(ContainerItemBag.of(
                items.stream()
                        .filter(item -> !item.done())
                        .collect(Collectors.toList())));
    }

    ContainerItemBag confirmQty(BigDecimal qty) {
        for (ContainerItem item : items) {
            if (item.getPendingQty().compareTo(qty) > 0) {
                item.confirmQty(qty);
                break;
            }

            // 需要分配给多个容器明细
            qty = item.confirmQty(qty);
        }

        return ContainerItemBag.of(items);
    }
}
