package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderLine;
import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IDomainModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 拣货容器里的商品和数量.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@KeyRelation(whom = OrderLine.class, type = KeyRelation.Type.BelongTo)
public class ContainerItem implements IDomainModel {
    private Long id;
    @KeyElement(types = KeyElement.Type.Structural)
    private Sku sku;

    /**
     * 预计的复核数量.
     */
    @KeyElement(types = KeyElement.Type.Quantity)
    private BigDecimal expectedQty;
    /**
     * 复核确认的数量.
     */
    @KeyElement(types = KeyElement.Type.Quantity)
    private BigDecimal givenQty;
    /**
     * 待复核数量.
     */
    @KeyElement(types = KeyElement.Type.Quantity)
    private BigDecimal pendingQty;

    private OrderLineNo orderLineNo;

    /**
     * 复核作业发现的差异数量.
     */
    public BigDecimal diffQty() {
        return expectedQty.subtract(givenQty);
    }

    public boolean done() {
        return pendingQty.compareTo(BigDecimal.ZERO) == 0;
    }

}
