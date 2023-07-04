package io.github.dddplus.model;

import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.NonNull;

import java.util.Set;

/**
 * SetBag is a handy memory Set based {@link IBag}.
 *
 * @param <Entity> The entity type
 */
public abstract class SetBag<Entity> implements IBag {
    protected final Set<Entity> items;

    protected SetBag(@NonNull Set<Entity> items) {
        this.items = items;
    }

    public final int size() {
        return items.size();
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    protected Entity anyItem() {
        return items.iterator().next();
    }

    public final void satisfy(@NonNull ISpecification<Entity> specification) {
        Notification notification = Notification.build();
        for (Entity entity : items) {
            if (!specification.isSatisfiedBy(entity, notification)) {
                whenNotSatisfied(notification);
                break;
            }
        }
    }

    protected abstract void whenNotSatisfied(Notification notification);

}
