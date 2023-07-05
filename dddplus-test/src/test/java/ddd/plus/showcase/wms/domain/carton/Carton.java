package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.carton.dict.CartonStatus;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.spcification.Notification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter(AccessLevel.PACKAGE)
public class Carton extends BaseAggregateRoot<Carton> implements IUnboundedDomainModel {
    private Long id;
    private CartonNo cartonNo;

    @KeyRelation(whom = Task.class, type = KeyRelation.Type.BelongTo)
    private TaskNo taskNo;
    @KeyRelation(whom = Order.class, type = KeyRelation.Type.BelongTo)
    private OrderNo orderNo;
    @KeyRelation(whom = Pallet.class, type = KeyRelation.Type.BelongTo, contextual = true)
    private PalletNo palletNo;

    @KeyElement(types = KeyElement.Type.Lifecycle)
    @Delegate
    private CartonStatus status;
    @KeyRelation(whom = CartonItemBag.class, type = KeyRelation.Type.HasOne)
    private CartonItemBag itemBag;
    @KeyRelation(whom = ConsumableBag.class, type = KeyRelation.Type.HasOne)
    private ConsumableBag consumableBag;

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    @KeyBehavior
    public void fulfill() {

    }
}
