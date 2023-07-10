package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.IDomainModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 发货的订单行，货品维度.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class OrderLineManifest implements IDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderLineNo orderLineNo;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Sku sku;
}
