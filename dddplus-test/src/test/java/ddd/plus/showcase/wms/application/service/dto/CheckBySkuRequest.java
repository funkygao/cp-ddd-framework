package ddd.plus.showcase.wms.application.service.dto;

import javax.validation.constraints.NotNull;
import ddd.plus.showcase.wms.application.service.dto.base.ApiRequest;
import lombok.Data;

@Data
public class CheckBySkuRequest extends ApiRequest {
    @NotNull
    private String taskNo;
    @NotNull
    private String orderNo;

    private String cartonNo;

    @NotNull
    private String skuNo;
    private String lotNo;
    private String packCode;
    private String uniqueCode; // 扫描得到的货品唯一码
    private String qty; // 为了降低 BigDecimal 反序列化性能损耗以及语言差异，使用String进行序列化传递

    private String operatorNo;
    private String platformNo;


}
