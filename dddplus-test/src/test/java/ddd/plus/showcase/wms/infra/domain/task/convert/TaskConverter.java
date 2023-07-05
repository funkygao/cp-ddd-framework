package ddd.plus.showcase.wms.infra.domain.task.convert;

import ddd.plus.showcase.wms.domain.task.Container;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskOfSku;
import ddd.plus.showcase.wms.infra.domain.task.ContainerPo;
import ddd.plus.showcase.wms.infra.domain.task.TaskPo;
import io.github.dddplus.dsl.KeyFlow;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskConverter {
    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    Task toTask(TaskPo po);

    TaskPo toPo(Task task);

    /**
     * 如何落库时处理查询和报表使用的冗余字段
     */
    @KeyFlow
    default TaskPo toPo(TaskOfSku taskOfSku) {
        Task task = taskOfSku.unbounded();
        TaskPo po = toPo(task);

        // 补充冗余字段内容
        po.setTotalPendingQty(task.totalPendingQty());
        po.setTotalQty(task.totalQty());
        po.setTotalSku(task.totalSku());
        return po;
    }

    List<Container> fromPoList(List<ContainerPo> containerPos);
}
