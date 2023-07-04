package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.spcification.Notification;

public class ShippingManifest extends BaseAggregateRoot<ShippingManifest> implements IUnboundedDomainModel {

    @Override
    protected void onNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }
}
