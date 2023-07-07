package io.github.dddplus.model;

import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.Builder;

/**
 * BaseAggregateRoot is a handy reusable {@link IAggregateRoot}.
 *
 * @param <Entity>
 */
public abstract class BaseAggregateRoot<Entity> implements IAggregateRoot {
    @Builder.Default
    protected DirtyMemento memento = new DirtyMemento();
    @Builder.Default
    protected Exchange exchange = new Exchange();

    protected final void dirty(IDirtyHint hint) {
        memento.register(hint);
    }

    protected final void mergeDirtyWith(IMergeAwareDirtyHint hint) {
        memento.merge(hint);
    }

    public <T extends IDirtyHint> T firstHintOf(Class<T> hintClass) {
        return memento.firstHintOf(hintClass);
    }

    /**
     * 业务归约要满足.
     *
     * @param specification 业务归约
     */
    public void assureSatisfied(ISpecification<Entity> specification) {
        Notification notification = Notification.build();
        if (!specification.isSatisfiedBy((Entity) this, notification)) {
            whenNotSatisfied(notification);
        }
    }

    /**
     * 业务规约没有满足时如何处理.
     *
     * @param notification 未满足原因
     */
    protected abstract void whenNotSatisfied(Notification notification);

    /**
     * 临时交换区赋值.
     */
    public void xSet(String key, Object value) {
        exchange.set(key, value);
    }

    /**
     * 从临时交换区里取值.
     */
    public <T> T xGet(String key, Class<T> valueType) throws ClassCastException {
        return exchange.get(key, valueType);
    }

    /**
     * 临时交换区方便的布尔类型取值.
     */
    public boolean xIs(String key) throws ClassCastException {
        return exchange.is(key);
    }
}
