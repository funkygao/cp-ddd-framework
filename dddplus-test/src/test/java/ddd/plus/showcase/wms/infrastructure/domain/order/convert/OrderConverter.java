package ddd.plus.showcase.wms.infrastructure.domain.order.convert;

import ddd.plus.showcase.wms.domain.task.Container;
import ddd.plus.showcase.wms.infrastructure.domain.task.ContainerPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConverter {
    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);


    List<Container> fromPoList(List<ContainerPo> containerPos);
}
