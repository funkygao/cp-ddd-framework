package ddd.plus.showcase.wms.domain.diff;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderLine;
import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IDomainModel;

public class ContainerDiffItem implements IDomainModel {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Sku sku;
    @KeyRelation(whom = OrderLine.class, type = KeyRelation.Type.BelongTo)
    private OrderLineNo orderLineNo;
}
