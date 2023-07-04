package ddd.plus.showcase.wms.domain.base;

import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import io.github.dddplus.model.IBag;
import lombok.Getter;

import java.util.List;

public abstract class ListBag<Entity, Ex> implements IBag {
    @Getter
    protected List<Entity> items;

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
