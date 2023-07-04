package ddd.plus.showcase.wms.infra.domain.task.convert;

import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infra.domain.task.TaskPo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskConverter {
    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    Task toTask(TaskPo po);
}
