package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.order.dict.OrderExchangeKey;
import ddd.plus.showcase.wms.domain.order.dict.OrderStatus;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.order.dict.ProductionStatus;
import ddd.plus.showcase.wms.domain.pack.Pack;
import ddd.plus.showcase.wms.domain.pack.PackBag;
import ddd.plus.showcase.wms.domain.pack.WaybillNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskBag;
import io.github.dddplus.dsl.KeyBehavior;
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

    @KeyElement(types = KeyElement.Type.Referential, byType = true)
    private Carrier carrier;
    @KeyElement(types = KeyElement.Type.Referential, byType = true)
    private Supplier supplier;
    @KeyElement(types = KeyElement.Type.Referential, byType = true)
    private Consignee consignee;
    @KeyElement(types = KeyElement.Type.Contextual, byType = true, remark = "OFC随单下发")
    private WaybillNo waybillNo;

    @KeyElement(types = KeyElement.Type.Operational, byType = true)
    private OrderConstraint constraint;
    public OrderConstraint constraint() {
        return constraint;
    }

    @Delegate
    @KeyRelation(whom = OrderLineBag.class, type = KeyRelation.Type.HasOne)
    private OrderLineBag orderLineBag;

    // ============
    // associations
    // ============

    public interface OrderPacks extends HasMany<Pack> {
        PackBag packBag();
    }

    @KeyRelation(whom = OrderPacks.class, type = KeyRelation.Type.Associate)
    private OrderPacks packs;

    public OrderPacks packs() {
        return packs;
    }

    public void injectOrderPacks(@NonNull Class<? extends IOrderRepository> __, OrderPacks orderPacks) {
        this.packs = orderPacks;
    }

    public interface OrderTasks extends HasMany<Task> {
        TaskBag taskBag();
    }

    @KeyRelation(whom = OrderTasks.class, type = KeyRelation.Type.Associate)
    private OrderTasks tasks;

    public OrderTasks tasks() {
        return tasks;
    }

    public void injectOrderTasks(@NonNull Class<? extends IOrderRepository> __, OrderTasks orderTasks) {
        this.tasks = orderTasks;
    }

    public interface OrderCartons extends HasMany<Carton> {
        /**
         * 该订单已经装箱的货品件数总和.
         */
        @KeyBehavior
        int totalCartonizedQty();
    }

    @KeyRelation(whom = OrderCartons.class, type = KeyRelation.Type.Associate)
    private OrderCartons cartons;

    public OrderCartons cartons() {
        return cartons;
    }
    public void injectOrderCartons(@NonNull Class<? extends IOrderRepository> __, OrderCartons orderCartons) {
        this.cartons = orderCartons;
    }

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
            return cartons.totalCartonizedQty();
        }

        return 0;
    }

    /**
     * 切换到预售场景.
     */
    @KeyBehavior
    public OrderOfPresale asPresale() {
        return new OrderOfPresale(this);
    }

    /**
     * 该订单已经被推荐到哪一个复核台.
     */
    @KeyRule
    public Platform recommendedPlatform() {
        return Platform.of(xGet(RecommendedPlatformNo, String.class));
    }

}
