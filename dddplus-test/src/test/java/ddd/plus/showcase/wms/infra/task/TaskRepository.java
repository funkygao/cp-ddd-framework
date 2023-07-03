package ddd.plus.showcase.wms.infra.task;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import io.github.design.ContainerNo;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository implements ITaskRepository {
    @Override
    public Task mustGet(TaskNo taskNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public Task mustGet(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public Task mustGet(TaskNo taskNo, OrderNo orderNo, Sku sku, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public void save(Task task) {
        ConfirmQtyHint confirmQtyHint = task.firstHintOf(ConfirmQtyHint.class);
        TaskDirtyHint taskDirtyHint = task.firstHintOf(TaskDirtyHint.class);
    }
}
