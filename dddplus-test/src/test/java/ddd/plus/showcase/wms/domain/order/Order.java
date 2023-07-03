package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IUnboundedDomainModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户的出库单.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter
@KeyRelation(whom = OrderLineBag.class, type = KeyRelation.Type.HasOne)
public class Order extends BaseAggregateRoot<Order> implements IUnboundedDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.DCU)
    private OrderNo orderNo;
    private OrderLineBag orderLineBag;
}
