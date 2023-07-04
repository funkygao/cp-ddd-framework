package ddd.plus.showcase.wms.domain.task.spec;

import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.extern.slf4j.Slf4j;

/**
 * 复核任务是否可以重新复核.
 */
@Slf4j
public class TaskCanRecheck extends AbstractSpecification<Task> {

    @Override
    public boolean isSatisfiedBy(Task task, Notification notification) {
        return task.status().canRecheck();
    }
}
