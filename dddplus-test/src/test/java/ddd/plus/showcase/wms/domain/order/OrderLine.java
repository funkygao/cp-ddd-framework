package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.IDomainModel;

public class OrderLine implements IDomainModel {
    private Long id;

    private OrderLineNo orderLineNo;
}
