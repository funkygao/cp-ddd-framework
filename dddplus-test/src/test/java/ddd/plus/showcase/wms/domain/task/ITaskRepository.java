package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.model.IRepository;
import io.github.design.ContainerNo;

public interface ITaskRepository extends IRepository {

    Task mustGet(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException;

    Task mustGet(TaskNo taskNo, OrderNo orderNo, Sku sku, WarehouseNo warehouseNo) throws WmsException;

    void save(Task task);

}
