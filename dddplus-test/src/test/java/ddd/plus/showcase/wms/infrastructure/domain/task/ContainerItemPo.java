package ddd.plus.showcase.wms.infrastructure.domain.task;

import ddd.plus.showcase.wms.domain.task.ContainerItem;
import io.github.dddplus.model.IPo;
import lombok.Data;

/**
 * {@link ContainerItem} persistent object.
 */
@Data
public class ContainerItemPo implements IPo {
    private Long id;
    private String taskNo;
    private String containerNo;
    private String warehouseNo;
}
