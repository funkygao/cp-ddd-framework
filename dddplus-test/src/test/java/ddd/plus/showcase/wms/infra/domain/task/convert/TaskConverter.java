package ddd.plus.showcase.wms.infra.domain.task.convert;

import ddd.plus.showcase.wms.domain.task.Container;
import ddd.plus.showcase.wms.domain.task.ContainerItem;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskOfSkuPending;
import ddd.plus.showcase.wms.infra.domain.task.ContainerItemPo;
import ddd.plus.showcase.wms.infra.domain.task.ContainerPo;
import ddd.plus.showcase.wms.infra.domain.task.TaskPo;
import ddd.plus.showcase.wms.infra.domain.task.TaskRepository;
import io.github.dddplus.dsl.KeyFlow;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskConverter {
    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    Task fromPo(TaskPo po);

    TaskPo toPo(Task task);

    /**
     * 如何落库时处理查询和报表使用的冗余字段
     */
    @KeyFlow(actor = TaskRepository.class)
    default TaskPo toPo(TaskOfSkuPending taskOfSkuPending) {
        Task task = taskOfSkuPending.unbounded();
        TaskPo po = toPo(task);

        // 补充冗余字段内容
        po.setTotalPendingQty(task.containerBag().totalPendingQty());
        po.setTotalQty(task.containerBag().totalQty());
        po.setTotalSku(task.containerBag().totalSku());
        return po;
    }

    Container fromPo(ContainerPo containerPo);

    List<Container> fromContainerPoList(List<ContainerPo> containerPos);

    List<ContainerItem> fromContainerItemPoList(List<ContainerItemPo> containerItemPoList);
}
