package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;
import io.github.dddplus.model.encapsulation.AllowedAccessors;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfOrderPending extends BoundedDomainModel<Task> {
    private final OrderNo orderNo;

    @AllowedAccessors(ITaskRepository.class)
    public TaskOfOrderPending(Task task, OrderNo orderNo) {
        this.model = task;
        this.orderNo = orderNo;
    }

    @KeyBehavior
    public CheckResult confirmQty(Operator operator, Platform platform) {
        Order order = unbounded().orders().pendingOrder(orderNo);
        return null;
    }
}
