package ddd.plus.showcase.wms.infra.domain.task;

import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import io.github.dddplus.model.IPo;
import lombok.Data;

/**
 * {@link ddd.plus.showcase.wms.domain.task.Task} persistent object.
 */
@Data
public class TaskPo implements IPo {
    private Long id;
    private String taskNo;
    private TaskStatus status; // 通过MyBatis typeHandler进行转换

    // 乐观锁
    private Short version;
}
