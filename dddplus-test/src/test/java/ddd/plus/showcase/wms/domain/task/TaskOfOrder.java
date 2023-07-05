package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfOrder extends BoundedDomainModel<Task> {
    private final OrderNo orderNo;

    public TaskOfOrder(Task task, OrderNo orderNo) {
        this.model = task;
        this.orderNo = orderNo;
    }

    @KeyBehavior
    public void confirmQty(Operator operator, Platform platformNo) {

    }
}
