package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;
import lombok.NonNull;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfContainer extends BoundedDomainModel<Task> {
    private final Container container;

    public TaskOfContainer(@NonNull Class<? extends ITaskRepository> __, Task task, Container container) {
        this.model = task;
        this.container = container;
    }

}
