package ddd.plus.showcase.wms.application.convert;

import ddd.plus.showcase.wms.application.service.dto.ShipOrderRequest;
import ddd.plus.showcase.wms.domain.ship.ShipManifest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShippingAppConverter {
    ShippingAppConverter INSTANCE = Mappers.getMapper(ShippingAppConverter.class);

    ShipManifest toShippingManifest(ShipOrderRequest dto);
}
