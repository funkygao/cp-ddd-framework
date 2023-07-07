package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.common.Sku;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IDomainModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单行.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class OrderLine implements IDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderLineNo orderLineNo;

    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Sku sku;

    @KeyElement(types = KeyElement.Type.Quantity, remark = "要货量")
    private BigDecimal qty;

    @KeyElement(types = KeyElement.Type.Quantity, remark = "缺货量")
    private BigDecimal shortageQty = BigDecimal.ZERO;

    @KeyRule(remark = "要货量-缺货量")
    BigDecimal expectedQty() {
        return qty.subtract(shortageQty);
    }

}
