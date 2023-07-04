package io.github.dddplus.model;

import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * ListBag is a handy memory List based {@link IBag}.
 *
 * @param <Entity> The entity type
 * @param <Ex> throws what type of exception if {@link #satisfy(ISpecification)} fails
 */
public abstract class ListBag<Entity, Ex> implements IBag {
    @Getter
    protected final List<Entity> items;

    protected ListBag(@NonNull List<Entity> items) {
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

    public final <Ex extends RuntimeException> void satisfy(@NonNull ISpecification<Entity> specification) throws Ex {
        Notification notification = Notification.build();
        for (Entity entity : items) {
            if (!specification.isSatisfiedBy(entity, notification)) {
                throw new RuntimeException(notification.first());
            }
        }
    }
}
