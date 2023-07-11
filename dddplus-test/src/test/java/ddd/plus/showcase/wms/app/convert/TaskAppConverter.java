package ddd.plus.showcase.wms.app.convert;

import ddd.plus.showcase.wms.app.service.dto.ContainerItemResult;
import ddd.plus.showcase.wms.app.worker.dto.SubmitTaskDto;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.infra.domain.task.ContainerItemPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskAppConverter {
    TaskAppConverter INSTANCE = Mappers.getMapper(TaskAppConverter.class);

    Task fromDto(SubmitTaskDto dto);

    List<ContainerItemResult> containerItemPoList2Dto(List<ContainerItemPo> poList);
}
