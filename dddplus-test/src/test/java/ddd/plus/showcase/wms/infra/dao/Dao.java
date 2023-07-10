package ddd.plus.showcase.wms.infra.dao;

import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infra.domain.carton.CartonPo;
import ddd.plus.showcase.wms.infra.domain.task.TaskPo;
import io.github.dddplus.model.IPo;
import io.github.dddplus.model.IRepository;

import java.util.List;

/**
 * 这里仅做演示，所以简化了：正式项目它对应的是各个{@link IRepository}.
 */
public interface Dao {
    <T> T query(String sql, Object... params);

    void insert(Task task);

    void insert(IPo po);

    void insert(List<? extends IPo> poList);

    // returns affected rows
    int update(TaskPo po);

    int update(CartonPo po);
}
