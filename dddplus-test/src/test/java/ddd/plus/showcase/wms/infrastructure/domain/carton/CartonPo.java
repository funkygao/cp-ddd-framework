package ddd.plus.showcase.wms.infrastructure.domain.carton;

import ddd.plus.showcase.wms.domain.carton.dict.CartonStatus;
import io.github.dddplus.model.IPo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartonPo implements IPo {
    private String orderNo;
    private BigDecimal checkedQty;
    private LocalDateTime fulfillTime;
    private CartonStatus cartonStatus;
    private String operatorNo;
    private String platformNo;
}
