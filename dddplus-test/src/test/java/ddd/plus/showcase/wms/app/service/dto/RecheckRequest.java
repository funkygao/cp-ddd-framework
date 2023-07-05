package ddd.plus.showcase.wms.app.service.dto;

import ddd.plus.showcase.wms.app.service.dto.base.ApiRequest;
import lombok.Data;

import java.util.List;

/**
 * 重新复核请求.
 */
@Data
public class RecheckRequest extends ApiRequest {
    private String taskNo;
    private String cartonNo;
    private List<Long> cartonItemIdList;
}
