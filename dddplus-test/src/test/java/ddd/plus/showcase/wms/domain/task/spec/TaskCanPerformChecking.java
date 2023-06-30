package ddd.plus.showcase.wms.domain.task.spec;

import ddd.plus.showcase.wms.domain.common.ExceptionCode;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.buddy.specification.AbstractSpecification;
import io.github.dddplus.buddy.specification.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskCanPerformChecking extends AbstractSpecification<Task> {
    @Override
    public boolean isSatisfiedBy(Task task, Notification notification) {
        if (task.getStatus().canPerformChecking()) {
            notification.addError(ExceptionCode.TaskCannotPerform.error());
            return false;
        }

        return true;
    }
}
