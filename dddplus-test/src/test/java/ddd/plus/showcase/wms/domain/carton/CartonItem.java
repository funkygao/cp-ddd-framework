package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.UniqueCode;
import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.IDomainModel;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 纸箱里个某一种sku.
 */
public class CartonItem implements IDomainModel {
    private Long id;

    @KeyElement
    private Sku sku;
    /**
     * 已复核数量.
     */
    @KeyElement(types = KeyElement.Type.Quantity)
    private BigDecimal checkedQty;
    @Getter
    private UniqueCode uniqueCode;

    @KeyElement(types = KeyElement.Type.Referential)
    private OrderLineNo orderLineNo;
}
