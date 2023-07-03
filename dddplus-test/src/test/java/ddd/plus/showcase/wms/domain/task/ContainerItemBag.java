package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.base.ListBag;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IUnboundedDomainModel;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@KeyRelation(whom = ContainerItem.class, type = KeyRelation.Type.HasMany)
public class ContainerItemBag extends ListBag<ContainerItem> implements IUnboundedDomainModel {

    private ContainerItemBag(List<ContainerItem> items) {
        this.items = items;
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
        BigDecimal total = BigDecimal.ZERO;
        for (ContainerItem item : items) {
            total = total.add(item.getExpectedQty());
        }
        return total;
    }

    // Yes, not public
    @KeyBehavior
    ContainerItemBag allocatedQty(BigDecimal confirmedQty) {
        return null;
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

    public void confirmQty(BigDecimal qty) {
        for (ContainerItem item : items) {
            if (item.getPendingQty().compareTo(qty) > 0) {
                item.confirmQty(qty);
                break;
            }

            // 需要分配给多个容器明细
            qty = item.confirmQty(qty);
        }
    }
}
