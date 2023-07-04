package ddd.plus.showcase.wms.infra.domain.task;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import io.github.dddplus.model.IPo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * {@link Task} persistent object.
 */
@Data
public class TaskPo implements IPo {
    private Long id;
    private String taskNo;
    private Integer priority;

    // MyBatis TypeHandler for the following enum
    private TaskMode taskMode;
    private Platform platformNo;
    private TaskStatus status;
    private Operator operator;
    private WarehouseNo warehouseNo;

    // 乐观锁，纯技术细节：领域层无感知
    private Short version;

    // ====================================
    // 除了领域模型概念外，复核任务还需要满足(查询，报表)等场景，如下字段是为了查询而在数据库里进行的冗余
    // ====================================
    private Integer totalSku; // 总品数
    private BigDecimal totalQty; // 总要货件数，each
    private BigDecimal totalPendingQty; // 总待复核件数，backlog

}
