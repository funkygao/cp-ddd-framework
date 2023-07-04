package ddd.plus.showcase.wms.infra.task;

import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.*;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import ddd.plus.showcase.wms.infra.dao.Dao;
import io.github.design.ContainerNo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository implements ITaskRepository {
    @Resource
    private Dao dao;

    @Override
    public Task mustGetPending(TaskNo taskNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public TaskOfContainer mustGetPending(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public TaskOfSku mustGetPending(TaskNo taskNo, OrderNo orderNo, Sku sku, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public Map<Platform, List<Task>> pendingTasksOfPlatforms(List<Platform> platformNos) {
        return null;
    }

    @Override
    public TaskBag tasksOfOrder(OrderNo orderNo, WarehouseNo warehouseNo) {
        return null;
    }

    @Override
    public void save(TaskOfSku task) {
        ConfirmQtyHint confirmQtyHint = task.unbounded().firstHintOf(ConfirmQtyHint.class);
        TaskDirtyHint taskDirtyHint = task.unbounded().firstHintOf(TaskDirtyHint.class);
    }

    @Override
    public void save(TaskOfContainer task) {

    }
}
