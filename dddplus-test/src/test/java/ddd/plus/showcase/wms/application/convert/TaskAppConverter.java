package ddd.plus.showcase.wms.application.convert;

import ddd.plus.showcase.wms.application.service.dto.ContainerItemResult;
import ddd.plus.showcase.wms.application.worker.dto.SubmitTaskDto;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infrastructure.domain.task.ContainerItemPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskAppConverter {
    TaskAppConverter INSTANCE = Mappers.getMapper(TaskAppConverter.class);

    Task fromDto(SubmitTaskDto dto);

    List<ContainerItemResult> containerItemPoList2Dto(List<ContainerItemPo> poList);
}
