package ddd.plus.showcase.wms.application.service.dto;

import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 按单发货请求.
 */
@Data
public class ShipOrderRequest extends ApiRequest {
    @NotEmpty
    private String orderNo;
    private String carrier;
    private String driverName;
    private String driverPhone;
    private String truckNo;

}
