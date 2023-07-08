package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.math.BigDecimal;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfSkuPending extends BoundedDomainModel<Task> {
    private final OrderNo orderNo;
    private final Sku sku;

    @Delegate
    public Task unbounded() {
        return super.unbounded();
    }

    public TaskOfSkuPending(@NonNull Class<ITaskRepository> __, Task task, OrderNo orderNo, Sku sku) {
        this.model = task;
        this.orderNo = orderNo;
        this.sku = sku;
    }

    @KeyBehavior
    public CheckResult confirmQty(BigDecimal qty, Operator operator, Platform platform) {
        Task task = unbounded();
        task.platform = platform;
        task.operator = operator;
        ContainerItemBag checkResult = task.containerBag().confirmQty(qty);
        task.dirtyMemento().register(new ConfirmQtyHint(task));
        return new CheckResult(checkResult);
    }

}
