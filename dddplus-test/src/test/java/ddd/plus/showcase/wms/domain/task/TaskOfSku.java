package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfSku extends BoundedDomainModel<Task> {
    private OrderNo orderNo;
    private Sku sku;

    protected TaskOfSku(Task model) {
        super(model);
    }

    public TaskOfSku(Task task, OrderNo orderNo, Sku sku) {
        this(task);
        this.orderNo = orderNo;
        this.sku = sku;
    }

}
