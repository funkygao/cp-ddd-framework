package ddd.plus.showcase.wms.infrastructure.domain.task.convert;

import ddd.plus.showcase.wms.domain.task.Container;
import ddd.plus.showcase.wms.domain.task.ContainerItem;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infrastructure.domain.task.ContainerItemPo;
import ddd.plus.showcase.wms.infrastructure.domain.task.ContainerPo;
import ddd.plus.showcase.wms.infrastructure.domain.task.TaskPo;
import ddd.plus.showcase.wms.infrastructure.domain.task.TaskRepository;
import io.github.dddplus.dsl.KeyFlow;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskConverter {
    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    Task fromPo(TaskPo po);

    /**
     * 如何落库时处理查询和报表使用的冗余字段
     */
    @KeyFlow(actor = TaskRepository.class)
    default TaskPo toPo(Task task) {
        TaskPo po = toPo(task);

        // 补充冗余字段内容
        po.setTotalPendingQty(task.totalPendingQty());
        po.setTotalQty(task.totalQty());
        po.setTotalSku(task.totalSku());
        return po;
    }

    List<ContainerPo> toContainerPoList(Task task, List<Container> containers);

    List<ContainerItemPo> toContainerItemPoList(Task task, List<ContainerItem> items);

    Container fromPo(ContainerPo containerPo);

    List<Container> fromContainerPoList(List<ContainerPo> containerPos);

    List<ContainerItem> fromContainerItemPoList(List<ContainerItemPo> containerItemPoList);
}
