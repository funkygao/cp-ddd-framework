package ddd.plus.showcase.wms.app.service.dto;

import ddd.plus.showcase.wms.app.service.dto.base.ApiRequest;
import lombok.Data;

/**
 * 推荐复核台请求.
 */
@Data
public class RecommendPlatformRequest extends ApiRequest {
    private String taskNo;
}
