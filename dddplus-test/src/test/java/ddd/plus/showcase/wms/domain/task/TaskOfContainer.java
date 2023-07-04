package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BoundedDomainModel;

@KeyRelation(whom = Task.class, type = KeyRelation.Type.Contextual)
public class TaskOfContainer extends BoundedDomainModel<Task> {
    private Container container;

    protected TaskOfContainer(Task model) {
        super(model);
    }

    public TaskOfContainer(Task task, Container container) {
        this(task);
        this.container = container;
    }

}
