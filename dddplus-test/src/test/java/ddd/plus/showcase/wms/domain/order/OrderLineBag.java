package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.ListBag;

import java.math.BigDecimal;
import java.util.List;

@KeyRelation(whom = OrderLine.class, type = KeyRelation.Type.HasMany)
public class OrderLineBag extends ListBag<OrderLine> {

    protected OrderLineBag(List<OrderLine> items) {
        super(items);
    }

    /**
     * 期望的总装箱货品件数.
     *
     * <p>要货量 - 缺货量</p>
     */
    @KeyRule
    public BigDecimal totalExpectedQty() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine orderLine : items) {
            total = total.add(orderLine.expectedQty());
        }
        return total;
    }
}
