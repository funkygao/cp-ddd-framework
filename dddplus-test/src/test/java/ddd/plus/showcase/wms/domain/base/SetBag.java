package ddd.plus.showcase.wms.domain.base;

import io.github.dddplus.buddy.specification.ISpecification;
import io.github.dddplus.buddy.specification.Notification;
import io.github.dddplus.model.IBag;

import java.util.Set;

public abstract class SetBag<Entity, Ex> implements IBag {
    protected Set<Entity> items;

    public final int size() {
        return items.size();
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    protected Entity anyItem() {
        return items.iterator().next();
    }

    public final <Ex extends RuntimeException> void satisfy(ISpecification<Entity> specification) throws Ex {
        Notification notification = Notification.build();
        for (Entity item : items) {
            if (!specification.isSatisfiedBy(item, notification)) {
                throw new RuntimeException(notification.first());
            }
        }
    }
}
