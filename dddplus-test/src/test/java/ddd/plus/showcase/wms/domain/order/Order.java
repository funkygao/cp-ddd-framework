package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.common.publisher.IEventPublisher;
import ddd.plus.showcase.wms.domain.order.dict.OrderExchangeKey;
import ddd.plus.showcase.wms.domain.order.dict.OrderStatus;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.order.dict.ProductionStatus;
import ddd.plus.showcase.wms.domain.order.event.OrderCheckedEvent;
import ddd.plus.showcase.wms.domain.order.event.OrderShippedEvent;
import ddd.plus.showcase.wms.domain.order.hint.OrderCheckedHint;
import ddd.plus.showcase.wms.domain.order.hint.OrderShippedHint;
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
import io.github.dddplus.model.encapsulation.AllowedAccessors;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户的出库单.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
public class Order extends BaseAggregateRoot<Order> implements IUnboundedDomainModel, OrderExchangeKey, IOrder {
    private Long id;

    @Getter
    private OrderNo orderNo;
    private OrderType orderType;
    @Getter
    private WarehouseNo warehouseNo;
    private LocalDateTime updateTime;
    private Operator lastOperator;
    @Getter
    private String feature; // 特征向量

    // 订单状态
    @KeyElement(types = KeyElement.Type.Lifecycle, byJavadoc = true)
    private OrderStatus status;
    @Getter
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

    @KeyRelation(whom = OrderPacks.class, type = KeyRelation.Type.Associate)
    private OrderPacks packs;

    @KeyRelation(whom = OrderTasks.class, type = KeyRelation.Type.Associate)
    private OrderTasks tasks;

    @KeyRelation(whom = OrderCartons.class, type = KeyRelation.Type.Associate)
    private OrderCartons cartons;

    private IEventPublisher eventPublisher;

    /**
     * 推荐该订单使用几个包裹：{@link Pack}.
     */
    @KeyBehavior
    public int recommendPackQty() {
        if (constraint.isUseOnePack()) {
            return 1;
        }

        if (constraint.isCollectConsumables()) {
            return cartons.totalCartonizedQty();
        }

        return 0;
    }

    @KeyBehavior(useRawArgs = true, produceEvent = OrderCheckedEvent.class, async = true)
    public void checkedBy(Operator operator) {
        this.lastOperator = operator;
        this.updateTime = LocalDateTime.now();
        this.productionStatus = ProductionStatus.Checked;
        dirty(new OrderCheckedHint(this));

        eventPublisher.publish(new OrderCheckedEvent(orderNo.value(), warehouseNo.value()));
    }

    @KeyBehavior
    public void ship(Operator operator) {
        productionStatus = ProductionStatus.Shipped;
        lastOperator = operator;

        dirty(new OrderShippedHint(this));

        // 回传上游系统
        eventPublisher.publish(new OrderShippedEvent(orderNo.value(), warehouseNo.value()));
    }

    public OrderOfPresale asPresale() {
        return new OrderOfPresale(this);
    }

    @KeyRule
    public BigDecimal totalExpectedQty() {
        return orderLineBag.totalExpectedQty();
    }

    /**
     * 该订单已经被推荐到哪一个复核台.
     */
    @KeyRule
    public Platform recommendedPlatform() {
        return Platform.of(xGet(RecommendedPlatformNo, String.class));
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    public interface OrderCartons extends HasMany<Carton> {
        /**
         * 该订单已经装箱的货品件数总和.
         */
        @KeyBehavior
        int totalCartonizedQty();
    }

    public OrderCartons cartons() {
        return cartons;
    }

    public interface OrderPacks extends HasMany<Pack> {
        PackBag packBag();
    }

    public OrderPacks packs() {
        return packs;
    }

    public interface OrderTasks extends HasMany<Task> {
        TaskBag taskBag();
    }

    public OrderTasks tasks() {
        return tasks;
    }

    @AllowedAccessors(IOrderRepository.class)
    public void injects(OrderPacks orderPacks, OrderTasks orderTasks,
                        OrderCartons orderCartons, IEventPublisher eventPublisher) {
        this.packs = orderPacks;
        this.tasks = orderTasks;
        this.cartons = orderCartons;
        this.eventPublisher = eventPublisher;
    }
}
