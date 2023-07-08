package ddd.plus.showcase.wms.app.service.dto;

import ddd.plus.showcase.wms.app.service.dto.base.ApiRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 操作员领取复核任务.
 */
@Data
public class CartonFullRequest extends ApiRequest {
    @NotNUll
    private String cartonNo;
    private String orderNo;
    private String platformNo;
    /**
     * 该箱内使用了哪些耗材.
     */
    private List<Consumable> consumables;

    public static class Consumable implements Serializable {
        private String skuNo;
        private Integer qty;
    }

}
