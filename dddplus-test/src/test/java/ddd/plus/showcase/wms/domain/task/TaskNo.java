package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class TaskNo extends AbstractBusinessNo<String> {
    private TaskNo(@NonNull String value) {
        super(value);
    }

    public static TaskNo of(@NonNull String taskNo) {
        return new TaskNo(taskNo);
    }

}
