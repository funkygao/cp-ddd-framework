package ddd.plus.showcase.wms.infra.domain.task.association;

import ddd.plus.showcase.wms.domain.carton.CartonItemBag;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infra.dao.Dao;
import io.github.dddplus.dsl.KeyFlow;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskCartonItemsDb implements Task.TaskCartonItems {
    private final Task task;
    private Dao dao;

    /**
     * 另外一个关联对象的实现.
     */
    @Override
    @KeyFlow
    public CartonItemBag cartonItemBag() {
        return dao.query("select * from ob_carton_item where warehouse_no=? and task_no=?",
                task.getWarehouseNo().value(),
                task.getTaskNo().value());
    }
}
