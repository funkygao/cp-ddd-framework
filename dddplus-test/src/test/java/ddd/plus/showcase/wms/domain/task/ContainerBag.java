package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.IBag;

import java.math.BigDecimal;
import java.util.List;

public class ContainerBag implements IBag {
    private List<Container> containers;

    /**
     * 该容器的总商品种类(品数).
     */
    public int totalSku() {
        int total = 0;
        for (Container container : containers) {
            total += container.getContainerItemBag().totalSku();
        }
        return total;
    }

    /**
     * 该容器的总要货量.
     */
    public BigDecimal totalQty() {
        BigDecimal total = BigDecimal.ZERO;
        for (Container container : containers) {
            total = total.add(container.getContainerItemBag().totalQty());
        }
        return total;
    }

}
