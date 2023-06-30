package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IBag;

import java.util.List;

@KeyRelation(whom = OrderLine.class, type = KeyRelation.Type.HasMany)
public class OrderLineBag implements IBag {
    private List<OrderLine> orderLines;
}
