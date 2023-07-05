package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfContainer extends BoundedDomainModel<Task> {
    private final Container container;

    public TaskOfContainer(Task task, Container container) {
        this.model = task;
        this.container = container;
    }

}
