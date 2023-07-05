package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public int totalSku() {
        return items.size();
    }

    /**
     * 该容器的总货量.
     */
    @KeyRule
    public BigDecimal totalQty() {
        return items.stream().map(ContainerItem::getExpectedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @KeyRule
    public BigDecimal totalPendingQty() {
        return items.stream().map(ContainerItem::getPendingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Set<OrderNo> orderNoSet() {
        Set<OrderNo> orderNos = new HashSet<>(size());
        for (ContainerItem item : items) {
            orderNos.add(item.getOrderLineNo().getOrderNo());
        }
        return orderNos;
    }

    public ContainerItemBagPending pendingBag() {
        List<ContainerItem> list = new ArrayList<>();
        for (ContainerItem item : items) {
            if (!item.done()) {
                list.add(item);
            }
        }
        return new ContainerItemBagPending(ContainerItemBag.of(list));
    }

    public ContainerItemBag confirmQty(BigDecimal qty) {
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
