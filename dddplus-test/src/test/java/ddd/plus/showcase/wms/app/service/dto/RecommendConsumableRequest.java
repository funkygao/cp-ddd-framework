package ddd.plus.showcase.wms.app.service.dto;

import ddd.plus.showcase.wms.app.service.dto.base.ApiRequest;
import lombok.Data;

/**
 * 操作员领取复核任务.
 */
@Data
public class RecommendConsumableRequest extends ApiRequest {
    private String taskNo;
    private String cartonNo;

}
