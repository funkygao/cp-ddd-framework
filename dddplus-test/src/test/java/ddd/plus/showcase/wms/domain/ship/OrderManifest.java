package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IDomainModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 一个订单的装车清单.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
@KeyRelation(whom = Carton.class, type = KeyRelation.Type.From)
public class OrderManifest implements IDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderNo orderNo;

    @KeyRelation(whom = OrderLineManifest.class, type = KeyRelation.Type.HasMany, contextual = true, remark = "包裹明细采集")
    private List<OrderLineManifest> orderLineManifests;
}
