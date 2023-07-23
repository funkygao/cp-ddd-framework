package ddd.plus.showcase.wms.application.service.dto;

import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 操作员领取复核任务.
 */
@Data
public class ShipOrderRequest extends ApiRequest {
    @NotEmpty
    private String orderNo;

}
