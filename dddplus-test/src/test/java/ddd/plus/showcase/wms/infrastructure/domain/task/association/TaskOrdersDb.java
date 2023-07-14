package ddd.plus.showcase.wms.infrastructure.domain.task.association;

import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBag;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskOrdersDb implements Task.TaskOrders {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private final Task task;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Dao dao;

    /**
     * 如何实现关联对象，注入dao和entity
     */
    @Override
    @KeyBehavior
    public OrderBag pendingOrders() {
        return dao.query("select * from ob_order where warehouse_no=? and order_no in ? and status in ?",
                task.getWarehouseNo().value(),
                task.orderNoSet(),
                TaskStatus.allowCheckStatus());
    }

    /**
     * 关联对象实现业务校验，避免逻辑泄露
     */
    @Override
    @KeyBehavior
    public Order pendingOrder(OrderNo orderNo) throws WmsException {
        if (!task.orderNoSet().contains(orderNo)) {
            // 该出库单不属于该任务
            throw new WmsException(WmsException.Code.InvalidOrderNo);
        }

        return dao.query("select * from ob_order where warehouse_no=? and order_no=?",
                task.getWarehouseNo().value(),
                orderNo.value());
    }
}
