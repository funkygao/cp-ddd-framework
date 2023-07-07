package ddd.plus.showcase.wms.app.service.dto;

import ddd.plus.showcase.wms.app.service.dto.base.ApiRequest;
import lombok.Data;

@Data
public class CheckByOrderRequest extends ApiRequest {
    private String taskNo;
    private String orderNo;
    private String operatorNo;
    private String platformNo;
    private String palletNo;
}
