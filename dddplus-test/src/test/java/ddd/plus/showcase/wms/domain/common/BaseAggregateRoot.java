package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.buddy.specification.ISpecification;
import io.github.dddplus.buddy.specification.Notification;
import io.github.dddplus.model.IAggregateRoot;

public abstract class BaseAggregateRoot<Entity> implements IAggregateRoot {

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
