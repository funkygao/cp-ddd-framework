package ddd.plus.showcase.wms.infrastructure.domain.common;

import io.github.dddplus.model.IPo;
import lombok.Data;

/**
 * 通用幂等表.
 */
@Data
public class UuidPo implements IPo {
    private Long id;
    private String uuid;
    private String bizNo;
    private Short type;
    private String warehouseNo;
}
