package ddd.plus.showcase.wms.domain.carton.convert;

import ddd.plus.showcase.wms.domain.carton.CartonItem;
import ddd.plus.showcase.wms.domain.task.ContainerItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartonConverter {
    CartonConverter INSTANCE = Mappers.getMapper(CartonConverter.class);

    @Mappings({
            @Mapping(target = "checkedQty", source = "expectedQty"),
    })
    List<CartonItem> containerItem2CartonItem(List<ContainerItem> containerItems);
}
