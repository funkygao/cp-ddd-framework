package ddd.plus.showcase.wms.infra.dao;

import ddd.plus.showcase.wms.infra.domain.task.ContainerItemPo;
import ddd.plus.showcase.wms.infra.domain.task.TaskPo;

import java.util.List;

/**
 * 这里仅做演示，所以简化了：正式项目需要为每一个PO(Persistent Object)建立Dao接口.
 */
public interface Dao {
    <T> T query(String sql, Object... params);

    void execute(String sql, Object... params);

    // returns affected rows
    int update(TaskPo taskPo);

    List<ContainerItemPo> listContainerItems(String orderNo, String warehouseNo);
}
