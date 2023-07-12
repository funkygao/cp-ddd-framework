package ddd.plus.showcase.wms.application.service.dto;

import javax.validation.constraints.NotNull;
import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

/**
 * 操作员领取复核任务.
 */
@Data
public class RecommendConsumableRequest extends ApiRequest {
    @NotNull
    private String taskNo;
    @NotNull
    private String cartonNo;

}
