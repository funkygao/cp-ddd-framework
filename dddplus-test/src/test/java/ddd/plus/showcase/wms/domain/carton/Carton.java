package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.carton.convert.CartonConverter;
import ddd.plus.showcase.wms.domain.carton.dict.CartonStatus;
import ddd.plus.showcase.wms.domain.carton.event.CartonFulfilledEvent;
import ddd.plus.showcase.wms.domain.carton.hint.CartonDirtyHint;
import ddd.plus.showcase.wms.domain.carton.spec.CartonizationRule;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.gateway.IRuleGateway;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.common.publisher.IEventPublisher;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.ContainerItemBag;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.BelongTo;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 纸箱，它会成为包裹.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
public class Carton extends BaseAggregateRoot<Carton> implements IUnboundedDomainModel {
    @Getter
    private Long id;
    private CartonNo cartonNo;

    @KeyRelation(whom = Task.class, type = KeyRelation.Type.BelongTo)
    private TaskNo taskNo;
    @Getter
    @KeyRelation(whom = Order.class, type = KeyRelation.Type.BelongTo)
    private OrderNo orderNo;
    @KeyRelation(whom = Pallet.class, type = KeyRelation.Type.HasOne, contextual = true, remark = "物理世界是属于关系")
    private PalletNo palletNo;
    @KeyElement(types = KeyElement.Type.Operational)
    private CartonizationRule cartonizationRule;
    @Getter
    @KeyElement(types = KeyElement.Type.Lifecycle)
    @Delegate
    private CartonStatus status;
    @KeyRelation(whom = CartonItemBag.class, type = KeyRelation.Type.HasOne)
    @Delegate
    private CartonItemBag itemBag;
    @Getter
    @KeyRelation(whom = ConsumableBag.class, type = KeyRelation.Type.HasOne)
    private ConsumableBag consumableBag;
    @Getter
    @KeyElement(types = KeyElement.Type.Location, byType = true)
    private Platform platform;
    @Getter
    private Operator operator;
    private WarehouseNo warehouseNo;
    @Getter
    @KeyElement(types = KeyElement.Type.KPI)
    private LocalDateTime fulfillTime;

    private IRuleGateway ruleGateway;
    private IEventPublisher eventPublisher;

    @KeyRelation(whom = CartonOrder.class, type = KeyRelation.Type.Associate)
    private CartonOrder order;

    /**
     * 向纸箱添加耗材.
     */
    @KeyBehavior
    public void installConsumables(List<Consumable> consumables) {
        consumables.forEach(c -> c.bind(this));
        this.consumableBag = new ConsumableBag(consumables);
        mergeDirtyWith(new CartonDirtyHint(this, CartonDirtyHint.Type.InstallConsumables));
    }

    /**
     * 装箱规则，利用gateway延迟加载，规则也是规约.
     */
    @KeyRule
    public CartonizationRule cartonizationRule() {
        if (cartonizationRule == null) {
            cartonizationRule = ruleGateway.fetchCartonizationRule(this);
        }
        return cartonizationRule;
    }

    @KeyBehavior
    public void bindOrder(@NonNull OrderNo orderNo, @NonNull BigDecimal checkedQty) {
        this.orderNo = orderNo;
        CartonDirtyHint hint = new CartonDirtyHint(this, CartonDirtyHint.Type.BindOrder);
        hint.setCheckedQty(checkedQty);
        mergeDirtyWith(hint);
    }

    @KeyBehavior(useRawArgs = true)
    public void transferFrom(ContainerItemBag containerItemBag) {
        List<CartonItem> cartonItems = CartonConverter.INSTANCE.containerItem2CartonItem(containerItemBag.items());
        itemBag.appendAll(cartonItems);
        CartonDirtyHint hint = new CartonDirtyHint(this, CartonDirtyHint.Type.FromContainer);
        mergeDirtyWith(hint);
    }

    /**
     * 箱满了
     */
    @KeyBehavior(produceEvent = CartonFulfilledEvent.class, async = true)
    public void fulfill(Operator operator, Platform platform) {
        this.operator = operator;
        this.platform = platform;
        this.status = CartonStatus.Full;
        this.fulfillTime = LocalDateTime.now();
        mergeDirtyWith(new CartonDirtyHint(this, CartonDirtyHint.Type.Fulfill));

        // with transactional mailbox pattern
        CartonFulfilledEvent event = new CartonFulfilledEvent();
        event.setCartonNo(cartonNo.value());
        event.setWarehouseNo(warehouseNo.value());
        eventPublisher.publish(event);
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    public interface CartonOrder extends BelongTo<Order> {
    }

    public CartonOrder order() {
        return order;
    }

    public void injectCartonOrder(@NonNull Class<? extends ICartonRepository> __, CartonOrder cartonOrder) {
        this.order = cartonOrder;
    }

    public void injectRuleGateway(@NonNull Class<? extends ICartonRepository> __, IRuleGateway ruleGateway) {
        this.ruleGateway = ruleGateway;
    }

    public void injectEventPublisher(@NonNull Class<? extends ICartonRepository> __, IEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
