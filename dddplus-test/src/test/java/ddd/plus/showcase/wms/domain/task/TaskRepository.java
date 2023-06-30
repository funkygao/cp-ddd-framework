package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.model.IRepository;
import io.github.design.ContainerNo;

public interface TaskRepository extends IRepository {

    Task mustGet(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException;

    Task mustGet(TaskNo taskNo, WarehouseNo warehouseNo) throws WmsException;

    void save(Task task);


}
