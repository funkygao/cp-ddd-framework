/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 聚合根的脏数据备忘录，对聚合根内部状态变化进行追踪.
 *
 * <p>提升系统性能，因为降低了数据库交互.</p>
 * <p>{@code DirtyMemento}被{@code Entity}组合进去，用来(主动)通知{@code Repository}哪些状态改变了(变脏了/新增了/删除了).</p>
 * <p>通过这个机制，{@code repo.save(entity);}可以通过一个save方法支持所有的状态改变场景.</p>
 * <p>{@link DirtyMemento}仅限于一个聚合根；对于跨聚合根的情况，使用{@link IUnitOfWork}.</p>
 * <p>进阶应用：不仅普通聚合根上使用，{@link IBag}上也可以.</p>
 * <pre>
 * {@code
 *
 * class OrderEntity {
 *     private DirtyMemento dirtyMemento = new DirtyMemento();
 *
 *     public void foo() {
 *         dirtyMemento.register(new FooHint(this));
 *     }
 *     public void bar() {
 *         dirtyMemento.merge(new BarHint(this));
 *     }
 * }
 * class OrderRepository {
 *     void persist(Order order) {
 *         DirtyMemento dirtyMemento = order.getDirtyMemento();
 *         if (dirtyMemento.isEmpty()) {
 *             return;
 *         }
 *         FooHint fooHint = dirtyMemento.firstHintOf(FooHint.class);
 *         dao.executeFoo(fooHint);
 *         BarHint barHint = dirtyMemento.firstHintOf(BarHint.class);
 *         dao.executeBar(barHint);
 *     }
 *     void persist(OrderBag bag) {
 *         RemoveOrderLineHint hint = dirtyMemento.firstHintOf(RemoveOrderLineHint.class);
 *         dao.batchUpdate(hint);
 *     }
 * }
 * class OrderBag implements IBag {
 *     private DirtyMemento dirtyMemento;
 *     public void removeOrderLine(OrderLine orderLine) {
 *         // ...
 *         dirtyMemento.register(new RemoveOrderLineHint(orderLine));
 *     }
 * }
 * }
 * </pre>
 */
public final class DirtyMemento {
    private final List<IDirtyHint> hints = new LinkedList<>();

    /**
     * 注册(追加)一个脏数据通知，以便{@code Repository}知道具体如何持久化.
     *
     * @param hint the dirty hint with necessary data
     */
    public void register(@NonNull IDirtyHint hint) {
        hints.add(hint);
    }

    /**
     * 合并脏数据通知：如未注册则注册，否则与现有那一个hint合并.
     *
     * <p>保证备忘录里，该{@link IMergeAwareDirtyHint}只保留一个</p>
     * <p>Merge on Write</p>
     *
     * @param hint 脏数据通知
     */
    public void merge(@NonNull IMergeAwareDirtyHint hint) {
        for (IDirtyHint existingHint : hints) {
            if (!existingHint.getClass().equals(hint.getClass())) {
                continue;
            }

            IMergeAwareDirtyHint existingMergeAwareHint = (IMergeAwareDirtyHint) existingHint;
            if (hint.getId().equals(existingMergeAwareHint.getId())) {
                // found it, trigger the hook
                hint.onMerge(existingMergeAwareHint);
                return;
            }
        }

        // not found
        register(hint);
    }

    /**
     * 当前的所有脏数据.
     *
     * @return all dirty hints
     */
    public List<IDirtyHint> dirtyHints() {
        return hints;
    }

    public void clear() {
        hints.clear();
    }

    public boolean isEmpty() {
        return hints.isEmpty();
    }

    public int size() {
        return hints.size();
    }

    /**
     * 根据指定hint class type，找到已经注册的第一个脏数据通知.
     *
     * @param hintClass concrete type of {@link IDirtyHint}
     * @param <T>       hint class type
     * @return null if not found. ONLY returns the first registered hint of specified type
     */
    public <T extends IDirtyHint> T firstHintOf(Class<T> hintClass) {
        Optional<IDirtyHint> dirtyHint = hints.stream().filter(hintClass::isInstance).findFirst();
        if (dirtyHint.isPresent()) {
            return (T) dirtyHint.get();
        } else {
            return null;
        }
    }

    /**
     * 根据指定hint class type，找到其所有的脏数据通知.
     *
     * @param hintClass concrete type of {@link IDirtyHint}
     * @param <T>       hint class type
     * @return will never returns null, but might be an empty list
     */
    @NonNull
    public <T extends IDirtyHint> List<T> dirtyHintsOf(Class<T> hintClass) {
        return (List<T>) hints.stream().filter(hintClass::isInstance).collect(Collectors.toList());
    }
}
