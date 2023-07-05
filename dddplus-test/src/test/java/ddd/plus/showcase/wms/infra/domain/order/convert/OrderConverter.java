package ddd.plus.showcase.wms.infra.domain.order.convert;

import ddd.plus.showcase.wms.domain.task.Container;
import ddd.plus.showcase.wms.infra.domain.task.ContainerPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConverter {
    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);


    List<Container> fromPoList(List<ContainerPo> containerPos);
}
