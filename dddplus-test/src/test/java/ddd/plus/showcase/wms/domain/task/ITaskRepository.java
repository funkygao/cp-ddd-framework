package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.model.IRepository;
import io.github.design.ContainerNo;

import java.util.List;
import java.util.Map;

public interface ITaskRepository extends IRepository {

    Task mustGet(TaskNo taskNo, WarehouseNo warehouseNo) throws WmsException;

    TaskOfContainer mustGetPending(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException;

    TaskOfSku mustGetPending(TaskNo taskNo, OrderNo orderNo, Sku sku, WarehouseNo warehouseNo) throws WmsException;

    TaskOfOrder mustGet(OrderNo orderNo, WarehouseNo warehouseNo) throws WmsException;

    Map<Platform, List<Task>> pendingTasksOfPlatforms(List<Platform> platformNos);

    TaskBag tasksOfOrder(OrderNo orderNo, WarehouseNo warehouseNo);

    void save(TaskOfSku task);

    void save(TaskOfContainer task);

}
