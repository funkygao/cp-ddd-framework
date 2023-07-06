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
import lombok.experimental.Delegate;

import java.math.BigDecimal;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfSkuPending extends BoundedDomainModel<Task> {
    private final DirtyMemento memento;
    private final OrderNo orderNo;
    private final Sku sku;

    @Delegate
    public Task unbounded() {
        return super.unbounded();
    }

    public TaskOfSkuPending(@NonNull Class<? extends ITaskRepository> __, Task task, OrderNo orderNo, Sku sku) {
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
        ContainerItemBag checkResult = task.containerBag().pendingItemBag().confirmQty(qty);
        memento.register(new ConfirmQtyHint(task));
        return checkResult;
    }

}
