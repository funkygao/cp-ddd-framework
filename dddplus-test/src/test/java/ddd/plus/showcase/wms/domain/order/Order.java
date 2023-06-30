package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import io.github.dddplus.buddy.IDirtyHint;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter
@KeyRelation(whom = OrderLineBag.class, type = KeyRelation.Type.HasOne)
public class Order extends BaseAggregateRoot<Order> {
    private Long id;

    @KeyElement(types = KeyElement.Type.DCU)
    private OrderNo orderNo;
    private OrderLineBag orderLineBag;

    public <T extends IDirtyHint> T firstHintOf(Class<T> hintClass) {
        return memento.firstHintOf(hintClass);
    }
}
