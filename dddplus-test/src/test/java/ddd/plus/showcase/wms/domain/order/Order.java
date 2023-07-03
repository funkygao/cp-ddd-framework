package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.base.BaseAggregateRoot;
import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.dict.OrderStatus;
import ddd.plus.showcase.wms.domain.order.dict.ProductionStatus;
import ddd.plus.showcase.wms.domain.pack.Pack;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.HasMany;
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
@KeyRelation(whom = Pack.class, type = KeyRelation.Type.HasMany)
public class Order extends BaseAggregateRoot<Order> implements IUnboundedDomainModel {
    private Long id;

    private OrderNo orderNo;
    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private OrderStatus status;
    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private ProductionStatus productionStatus;
    @KeyElement(types = KeyElement.Type.Operational, byType = true)
    private OrderConstraint constraint;

    private OrderLineBag orderLineBag;
    private WarehouseNo warehouseNo;
    private Operator lastOperator;

    private OrderCartons cartons;

    public interface OrderCartons extends HasMany<Carton> {
        /**
         * 该订单已经装箱的货品件数总和.
         */
        int totalCartonizedQty();
    }


    @KeyBehavior
    public void pause(Operator operator) {
        lastOperator = operator;
    }

    @KeyBehavior
    public void resume(Operator operator) {
        lastOperator = operator;
    }
}
