package ddd.plus.showcase.wms.application.service.dto;

import javax.validation.constraints.NotNull;
import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

@Data
public class CheckByOrderRequest extends ApiRequest {
    @NotNull
    private String taskNo;

    @NotNull
    private String orderNo;
    private String operatorNo;
    private String platformNo;
    private String palletNo;
}
