package ddd.plus.showcase.wms.infra.domain.task.association;

import ddd.plus.showcase.wms.domain.carton.CartonItemBag;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infra.dao.Dao;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskCartonItemsDb implements Task.TaskCartonItems {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private final Task task;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Dao dao;

    /**
     * 另外一个关联对象的实现.
     */
    @Override
    @KeyBehavior
    public CartonItemBag cartonItemBag() {
        return dao.query("select * from ob_carton_item where warehouse_no=? and task_no=?",
                task.getWarehouseNo().value(),
                task.getTaskNo().value());
    }
}
