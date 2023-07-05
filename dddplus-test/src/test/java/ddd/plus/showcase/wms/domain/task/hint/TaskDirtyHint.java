package ddd.plus.showcase.wms.domain.task.hint;

import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.IDirtyHint;
import io.github.dddplus.model.IMergeAwareDirtyHint;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class TaskDirtyHint implements IMergeAwareDirtyHint<Long> {
    private final Task task;
    private final Set<String> dirtyFields = new HashSet<>();

    public TaskDirtyHint(Task task) {
        this.task = task;
    }

    public TaskDirtyHint dirty(String... fields) {
        for (String field : fields) {
            dirtyFields.add(field);
        }
        return this;
    }

    @Override
    public void onMerge(IDirtyHint thatHint) {
        TaskDirtyHint that = (TaskDirtyHint) thatHint;
        dirtyFields.addAll(that.dirtyFields);
    }

    @Override
    public Long getId() {
        return task.getId();
    }
}
