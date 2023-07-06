package ddd.plus.showcase.wms.infra.domain.carton.convert;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonItem;
import ddd.plus.showcase.wms.domain.carton.Consumable;
import ddd.plus.showcase.wms.infra.domain.carton.CartonItemPo;
import ddd.plus.showcase.wms.infra.domain.carton.CartonPo;
import ddd.plus.showcase.wms.infra.domain.carton.ConsumablePo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartonConverter {
    CartonConverter INSTANCE = Mappers.getMapper(CartonConverter.class);

    Carton fromPo(CartonPo po);

    List<ConsumablePo> toPo(List<Consumable> consumables);

    List<CartonItemPo> toPo(List<CartonItem> cartonItems);
}
