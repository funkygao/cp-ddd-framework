package ddd.plus.showcase.wms.application.service.dto;

import javax.validation.constraints.NotNull;;
import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

;

/**
 * 推荐复核台请求.
 */
@Data
public class RecommendPlatformRequest extends ApiRequest {
    @NotNull
    private String taskNo;
    private String taskMode;

    private String orderNo;
    private String orderType;
}
