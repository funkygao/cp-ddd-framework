package io.github.dddplus.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * ListBag is a handy memory List based {@link IBag}.
 *
 * @param <Entity> The entity type
 */
public abstract class ListBag<Entity> implements IBag {
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

    /**
     * 返回任意一个对象：在与具体对象无关场景使用.
     */
    protected Entity anyItem() {
        return items.iterator().next();
    }

    public final boolean contains(Entity entity) {
        return items.contains(entity);
    }
}
