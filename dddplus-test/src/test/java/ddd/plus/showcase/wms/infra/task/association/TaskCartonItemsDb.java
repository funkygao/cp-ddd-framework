package ddd.plus.showcase.wms.infra.task.association;

import ddd.plus.showcase.wms.domain.carton.CartonItemBag;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infra.dao.Dao;
import io.github.dddplus.dsl.KeyFlow;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class TaskCartonItemsDb implements Task.TaskCartonItems {
    private final Task task;
    @Autowired
    private Dao dao;

    @Override
    @KeyFlow(actor = Task.class)
    public CartonItemBag cartonItemBag() {
        return dao.query("select * from ob_carton_item where warehouse_no=? and task_no=?",
                task.getWarehouseNo().value(),
                task.getTaskNo().value());
    }
}
