package ddd.plus.showcase.wms.application.service.dto;

import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 操作员领取复核任务.
 */
@Data
public class ClaimTaskRequest extends ApiRequest {
    @NotNull
    private String containerNo;
    private String platformNo;
    // 领取任务时是否提示打成几个包裹
    public Boolean recommendPackQty;

}
