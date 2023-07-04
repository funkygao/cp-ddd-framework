package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.base.BaseAggregateRoot;
import ddd.plus.showcase.wms.domain.carton.dict.CartonStatus;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IUnboundedDomainModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Delegate;

@KeyRelation(whom = CartonItemBag.class, type = KeyRelation.Type.HasOne)
@KeyRelation(whom = ConsumableBag.class, type = KeyRelation.Type.HasOne)
@KeyRelation(whom = Pallet.class, type = KeyRelation.Type.BelongTo, remark = "可能")
@KeyRelation(whom = Task.class, type = KeyRelation.Type.BelongTo)
@KeyRelation(whom = Order.class, type = KeyRelation.Type.BelongTo)
@Getter(AccessLevel.PACKAGE)
public class Carton extends BaseAggregateRoot<Carton> implements IUnboundedDomainModel {
    private Long id;
    private CartonNo cartonNo;

    private TaskNo taskNo;
    private OrderNo orderNo;

    @KeyElement(types = KeyElement.Type.Lifecycle)
    @Delegate
    private CartonStatus status;

    private CartonItemBag itemBag;
    private ConsumableBag consumableBag;

    @KeyBehavior
    public void fullize() {

    }
}
