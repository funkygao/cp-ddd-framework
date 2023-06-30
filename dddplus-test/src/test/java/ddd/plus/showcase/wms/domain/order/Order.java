package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import io.github.dddplus.buddy.DirtyMemento;
import io.github.dddplus.buddy.Exchange;
import io.github.dddplus.dsl.KeyRelation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@KeyRelation(whom = OrderLine.class, type = KeyRelation.Type.HasMany)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Order extends BaseAggregateRoot<Order> {
    @Builder.Default
    private DirtyMemento dirtyMemento = new DirtyMemento();
    @Builder.Default
    private Exchange exchange = new Exchange();

    private Long id;
    private OrderNo orderNo;
    private OrderLineBag orderLineBag;
}
