package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import ddd.plus.showcase.wms.domain.task.event.TaskAcceptedEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskAcceptedEventHandler extends AbstractEventHandler<TaskAcceptedEvent> {
    private ITaskRepository taskRepository;

    @Override
    protected void processMyEvent(TaskAcceptedEvent event) {
        Task task = taskRepository.mustGet(TaskNo.of(event.getTaskNo()), WarehouseNo.of(event.getWarehouseNo()));
        if (task.mode().isLabelPicking()) {
            // 标签拣选，是不需要复核的，拣货后直接发货，因此复核只走自动化的数据流
            // 自动复核

            triggerSuccessor(event);
        }
    }

    @Override
    protected boolean isMine(IFlowAutomationEvent event) {
        return event instanceof TaskAcceptedEvent;
    }
}
