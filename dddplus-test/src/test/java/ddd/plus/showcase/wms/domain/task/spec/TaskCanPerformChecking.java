package ddd.plus.showcase.wms.domain.task.spec;

import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskCanPerformChecking extends AbstractSpecification<Task> {
    @Override
    public boolean isSatisfiedBy(Task task, Notification notification) {
        if (task.status().canPerformChecking()) {
            notification.addError(WmsException.Code.TaskCannotPerform.getErrorCode());
            return false;
        }

        return true;
    }
}
