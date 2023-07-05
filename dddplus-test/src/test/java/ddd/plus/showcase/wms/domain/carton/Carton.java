package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.carton.convert.CartonConverter;
import ddd.plus.showcase.wms.domain.carton.dict.CartonStatus;
import ddd.plus.showcase.wms.domain.carton.hint.CaronDirtyHint;
import ddd.plus.showcase.wms.domain.carton.spec.CartonizationRule;
import ddd.plus.showcase.wms.domain.common.IRuleGateway;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WmsException;
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
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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
    @KeyRelation(whom = Order.class, type = KeyRelation.Type.BelongTo)
    private OrderNo orderNo;
    @KeyRelation(whom = Pallet.class, type = KeyRelation.Type.BelongTo, contextual = true)
    private PalletNo palletNo;
    @KeyElement(types = KeyElement.Type.Operational)
    private CartonizationRule cartonizationRule;
    @KeyElement(types = KeyElement.Type.Lifecycle)
    @Delegate
    private CartonStatus status;
    @KeyRelation(whom = CartonItemBag.class, type = KeyRelation.Type.HasOne)
    private CartonItemBag itemBag;
    @KeyRelation(whom = ConsumableBag.class, type = KeyRelation.Type.HasOne)
    private ConsumableBag consumableBag;

    private IRuleGateway ruleGateway;

    public void injectRuleGateway(Class<? extends ICartonRepository> _any, IRuleGateway ruleGateway) {
        this.ruleGateway = ruleGateway;
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

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    @KeyBehavior
    public void bindOrder(@NonNull OrderNo orderNo, @NonNull BigDecimal checkedQty) {
        this.orderNo = orderNo;
        CaronDirtyHint hint = new CaronDirtyHint(this, CaronDirtyHint.Type.BindOrder);
        hint.setCheckedQty(checkedQty);
        mergeDirty(hint);
    }

    @KeyBehavior(useRawArgs = true)
    public void transferFrom(ContainerItemBag containerItemBag) {
        List<CartonItem> cartonItems = CartonConverter.INSTANCE.containerItem2CartonItem(containerItemBag.getItems());
        itemBag.appendAll(cartonItems);
        // 这里 hint 与 bindOrder 合并
        CaronDirtyHint hint = new CaronDirtyHint(this, CaronDirtyHint.Type.TransferFrom);
        mergeDirty(hint);
    }

    /**
     * 箱满了
     */
    @KeyBehavior
    public void fulfill(Operator operator, Platform platform) {

    }
}
