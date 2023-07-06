package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;
import lombok.NonNull;
import lombok.experimental.Delegate;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfContainerPending extends BoundedDomainModel<Task> {
    private final Container container;

    public TaskOfContainerPending(@NonNull Class<? extends ITaskRepository> __, Task task, Container container) {
        this.model = task;
        this.container = container;
    }

    @Delegate
    public Task unbounded() {
        return super.unbounded();
    }

}
