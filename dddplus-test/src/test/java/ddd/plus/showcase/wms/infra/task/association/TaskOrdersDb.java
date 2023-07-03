package ddd.plus.showcase.wms.infra.task.association;

import ddd.plus.showcase.wms.domain.common.ExceptionCode;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBag;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import ddd.plus.showcase.wms.infra.dao.Dao;
import io.github.dddplus.dsl.KeyFlow;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class TaskOrdersDb implements Task.TaskOrders {
    private final Task task;
    @Autowired
    private Dao dao;

    @Override
    @KeyFlow(actor = Task.class)
    public OrderBag pendingOrders() {
        return dao.query("select * from ob_order where warehouse_no=? and order_no in ? and status in ?",
                task.getWarehouseNo().value(),
                task.getContainerBag().orderNoSet(),
                TaskStatus.allowCheckStatus());
    }

    @Override
    @KeyFlow(actor = Task.class)
    public Order pendingOrder(OrderNo orderNo) {
        if (!task.orderNoSet().contains(orderNo)) {
            // 该出库单不属于该任务
            throw new WmsException(ExceptionCode.InvalidOrderNo);
        }

        return dao.query("select * from ob_order where warehouse_no=? and order_no=?",
                task.getWarehouseNo().value(),
                orderNo.value());
    }
}
