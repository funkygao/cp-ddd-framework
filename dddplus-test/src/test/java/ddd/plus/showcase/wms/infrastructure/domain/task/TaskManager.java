package ddd.plus.showcase.wms.infrastructure.domain.task;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import io.github.dddplus.model.IManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TaskManager implements IManager {
    @Resource
    private Dao dao;

    public List<ContainerItemPo> listContainerItemsBy(TaskNo taskNo, WarehouseNo warehouseNo) {
        return dao.query("where task_no=? and warehouse_no=?",
                taskNo.value(),
                warehouseNo.value());
    }

}
