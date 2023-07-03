package ddd.plus.showcase.wms.domain.base;

import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.buddy.DirtyMemento;
import io.github.dddplus.buddy.Exchange;
import io.github.dddplus.buddy.IDirtyHint;
import io.github.dddplus.buddy.specification.ISpecification;
import io.github.dddplus.buddy.specification.Notification;
import io.github.dddplus.model.IAggregateRoot;
import lombok.Builder;

public abstract class BaseAggregateRoot<Entity> implements IAggregateRoot {
    @Builder.Default
    protected DirtyMemento memento = new DirtyMemento();
    @Builder.Default
    protected Exchange exchange = new Exchange();

    protected final <T> T dirty(IDirtyHint hint) {
        memento.register(hint);
        return (T) this;
    }

    public <T extends IDirtyHint> T firstHintOf(Class<T> hintClass) {
        return memento.firstHintOf(hintClass);
    }

    /**
     * 业务归约要满足.
     *
     * @param specification 业务归约
     * @throws WmsException 如果违反，则抛出的异常，里面包含了错误码和提示信息
     */
    public void assureSatisfied(ISpecification<Entity> specification) throws WmsException {
        Notification notification = Notification.build();
        if (!specification.isSatisfiedBy((Entity) this, notification)) {
            throw new WmsException(notification.first());
        }
    }
}
