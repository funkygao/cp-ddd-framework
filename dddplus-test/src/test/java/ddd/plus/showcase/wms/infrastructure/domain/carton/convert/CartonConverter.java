package ddd.plus.showcase.wms.infrastructure.domain.carton.convert;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonItem;
import ddd.plus.showcase.wms.domain.carton.Consumable;
import ddd.plus.showcase.wms.infrastructure.domain.carton.CartonItemPo;
import ddd.plus.showcase.wms.infrastructure.domain.carton.CartonPo;
import ddd.plus.showcase.wms.infrastructure.domain.carton.ConsumablePo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartonConverter {
    CartonConverter INSTANCE = Mappers.getMapper(CartonConverter.class);

    Carton fromPo(CartonPo po);

    List<ConsumablePo> toConsumablePo(List<Consumable> consumables);

    List<CartonItemPo> toCartonItemPo(List<CartonItem> cartonItems);
}
