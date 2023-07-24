package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IDomainModel;
import lombok.*;

import java.util.List;

/**
 * 一个订单的装车清单：纸箱维度.
 *
 * <p>如果一个订单有多个纸箱，则有多条{@link OrderCarton}.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@KeyRelation(whom = Carton.class, type = KeyRelation.Type.From)
public class OrderCarton implements IDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderNo orderNo;

    private CartonNo cartonNo;

    @KeyRelation(whom = OrderLineManifest.class, type = KeyRelation.Type.HasMany, contextual = true, remark = "包裹明细采集")
    private List<OrderLineManifest> orderLineManifests;
}
