package ddd.plus.showcase.wms.infrastructure.domain.task.association;

import ddd.plus.showcase.wms.domain.common.UniqueCode;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TaskCartonsDb implements Task.TaskCartons {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private final Task task;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Dao dao;

    /**
     * 另外一个关联对象的实现.
     */
    @KeyBehavior
    @Override
    public boolean contains(@NonNull UniqueCode uniqueCode) {
        Integer rowId = dao.query("select id from ob_carton_item where warehouse_no=? and task_no=? and unique_code=? limit 1",
                task.getWarehouseNo().value(),
                task.getTaskNo().value(),
                uniqueCode.value());
        return rowId != null;
    }
}
