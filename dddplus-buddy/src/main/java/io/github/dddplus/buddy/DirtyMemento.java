package io.github.dddplus.buddy;

import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 脏数据备忘录，服务于聚合根，帮助它对内部变化的对象进行追踪.
 *
 * <p>DDD里，entity在内存操作，改变状态，最后{@code repo.save(entity);} </p>
 * <p>{@code DirtyMemento}被Entity用来(主动)通知Repository哪些状态改变了(变脏了)</p>
 * <p>通过这个机制，{@code repo.save(entity);}可以通过一个save方法支持所有的状态改变场景</p>
 */
public final class DirtyMemento {
    private final List<IDirtyHint> hints = new LinkedList<>();

    /**
     * 注册一个脏数据通知.
     *
     * @param hint the dirty hint with necessary data
     */
    public void registerHint(@NonNull IDirtyHint hint) {
        hints.add(hint);
    }

    /**
     * 合并脏数据通知：如未注册则注册，否则与现有那一个hint合并.
     *
     * <p>保证备忘录里，该{@link IMergeableDirtyHint}只保留一个</p>
     * <p>Merge on Write</p>
     *
     * @param hint 脏数据通知
     */
    public void registerOrMerge(@NonNull IMergeableDirtyHint hint) {
        for (IDirtyHint existingHint : hints) {
            if (!existingHint.getClass().equals(hint.getClass())) {
                continue;
            }

            IMergeableDirtyHint existingMergeableHint = (IMergeableDirtyHint) existingHint;
            if (hint.getId().equals(existingMergeableHint.getId())) {
                // found it, trigger the hook
                hint.onMerge(existingMergeableHint);
                return;
            }
        }

        // not found
        registerHint(hint);
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
     * 根据指定hint class type，找到已经注册的最后一个脏数据通知.
     *
     * <p>例如，在一个请求内状态多次修改，只希望获取最新(最后)状态.</p>
     *
     * @param hintClass concrete type of {@link IDirtyHint}
     * @param <T>       hint class type
     * @return null if not found. ONLY returns the last registered hint of specified type
     */
    public <T extends IDirtyHint> T lastHintOf(Class<T> hintClass) {
        List<T> dirtyHints = dirtyHintsOf(hintClass);
        if (dirtyHints.isEmpty()) {
            return null;
        }

        return dirtyHints.get(dirtyHints.size() - 1);
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
