package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.IBag;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public class ContainerItemBag implements IBag {
    private final List<ContainerItem> items;

    private ContainerItemBag(List<ContainerItem> items) {
        this.items = items;
    }

    public static ContainerItemBag of(@NonNull List<ContainerItem> items) {
        return new ContainerItemBag(items);
    }

    /**
     * 该容器的总商品种类(品数).
     */
    public int totalSku() {
        return items.size();
    }

    /**
     * 该容器的总货量.
     */
    public BigDecimal totalQty() {
        BigDecimal total = BigDecimal.ZERO;
        for (ContainerItem item : items) {
            total = total.add(item.getExpectedQty());
        }
        return total;
    }

}
