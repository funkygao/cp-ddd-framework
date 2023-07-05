package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonItemBag;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.dict.OrderExchangeKey;
import ddd.plus.showcase.wms.domain.order.dict.OrderStatus;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.order.dict.ProductionStatus;
import ddd.plus.showcase.wms.domain.pack.Pack;
import ddd.plus.showcase.wms.domain.task.ContainerItem;
import ddd.plus.showcase.wms.domain.task.ContainerItemBag;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.HasMany;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户的出库单.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
@KeyRelation(whom = Pack.class, type = KeyRelation.Type.HasMany)
public class Order extends BaseAggregateRoot<Order> implements IUnboundedDomainModel, OrderExchangeKey {
    private Long id;

    @Getter
    private OrderNo orderNo;
    private OrderType orderType;
    @Getter
    private WarehouseNo warehouseNo;
    private Operator lastOperator;

    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private OrderStatus status;
    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private ProductionStatus productionStatus;

    @KeyElement(types = KeyElement.Type.Operational, byType = true)
    private OrderConstraint constraint;

    public OrderConstraint constraint() {
        return constraint;
    }

    @Delegate
    @KeyRelation(whom = OrderLineBag.class, type = KeyRelation.Type.HasOne)
    private OrderLineBag orderLineBag;

    // associations
    public interface OrderCartons extends HasMany<Carton> {
        /**
         * 该订单已经装箱的货品件数总和.
         */
        @KeyRule // FIXME not shown in uml
        int totalCartonizedQty();

        CartonItemBag cartonItemBag();
    }
    @Delegate
    @KeyRelation(whom = Order.OrderCartons.class, type = KeyRelation.Type.Associate)
    private OrderCartons cartons;

    public interface OrderContainerItems extends HasMany<ContainerItem> {
        ContainerItemBag containerItemBag();
    }
    @Delegate
    @KeyRelation(whom = OrderContainerItems.class, type = KeyRelation.Type.Associate)
    private OrderContainerItems containerItems;

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    /**
     * 推荐该订单使用几个包裹：{@link Pack}.
     */
    public int recommendPackQty() {
        if (constraint.isUseOnePack()) {
            return 1;
        }

        if (constraint.isCollectConsumables()) {
            return totalCartonizedQty();
        }

        return 0;
    }

    /**
     * 该订单已经被推荐到哪一个复核台.
     */
    @KeyRule
    public Platform recommendedPlatformNo() {
        return Platform.of(xGet(RecommendedPlatformNo, String.class));
    }
}
