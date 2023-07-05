package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;
import io.github.dddplus.model.DirtyMemento;
import lombok.NonNull;

import java.math.BigDecimal;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfSku extends BoundedDomainModel<Task> {
    private final DirtyMemento memento;
    private final OrderNo orderNo;
    private final Sku sku;

    public TaskOfSku(@NonNull Class<? extends ITaskRepository> _any, Task task, OrderNo orderNo, Sku sku) {
        this.model = task;
        this.orderNo = orderNo;
        this.sku = sku;
        this.memento = task.dirtyMemento();
    }

    @KeyBehavior
    public ContainerItemBag confirmQty(BigDecimal qty, Operator operator, Platform platformNo) {
        Task task = unbounded();
        task.platformNo = platformNo;
        task.operator = operator;
        // lombok Delegate，只能在外边调用时，才会Decorate：这里不能绕过containerBag直接调用pendingItemBag()
        ContainerItemBag checkResult = task.getContainerBag().pendingItemBag().confirmQty(qty);
        memento.register(new ConfirmQtyHint(task));
        return checkResult;
    }

}
