package io.github.dddplus.model;

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

    public final Set<Entity> items() {
        return items;
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 返回任意一个对象：在与具体对象无关场景使用.
     */
    public final Entity anyOne() {
        return items.iterator().next();
    }

    public final boolean contains(Entity entity) {
        return items.contains(entity);
    }

}
