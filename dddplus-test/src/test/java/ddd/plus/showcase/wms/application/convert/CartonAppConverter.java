package ddd.plus.showcase.wms.application.convert;

import ddd.plus.showcase.wms.application.service.dto.CheckByOrderRequest;
import ddd.plus.showcase.wms.domain.carton.Carton;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartonAppConverter {
    CartonAppConverter INSTANCE = Mappers.getMapper(CartonAppConverter.class);

    // 这里演示应用层把DTO转换为领域对象
    List<Carton> fromDto(CheckByOrderRequest dto);
}
