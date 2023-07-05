package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
public class ShippingManifest extends BaseAggregateRoot<ShippingManifest> implements IUnboundedDomainModel {
    @Getter
    private Long id;

    @KeyElement(types = KeyElement.Type.Structural)
    private String carrierNo;
    @KeyRelation(whom = OrderManifest.class, type = KeyRelation.Type.HasMany)
    private List<OrderManifest> orderManifests;
    @KeyElement(types = KeyElement.Type.Reserved)
    private Map<String, Object> extInfo;

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }
}
