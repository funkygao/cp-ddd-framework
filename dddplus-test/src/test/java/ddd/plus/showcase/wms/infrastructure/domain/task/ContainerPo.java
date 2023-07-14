package ddd.plus.showcase.wms.infrastructure.domain.task;

import ddd.plus.showcase.wms.domain.task.Container;
import io.github.dddplus.model.IPo;
import lombok.Data;

/**
 * {@link Container} persistent object.
 */
@Data
public class ContainerPo implements IPo {
    private Long id;
    private String taskNo;
    private String containerNo;
    private String warehouseNo;

}
