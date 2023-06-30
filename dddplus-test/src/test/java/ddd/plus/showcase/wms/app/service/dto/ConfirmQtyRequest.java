package ddd.plus.showcase.wms.app.service.dto;

import ddd.plus.showcase.wms.app.service.dto.base.ApiRequest;
import lombok.Data;

/**
 * 执行某一个复核任务，确认货品数量.
 */
@Data
public class ConfirmQtyRequest extends ApiRequest {
    private String taskNo;
    private String orderNo;
    private String skuNo;
    private String operatorNo;
    private String platformNo;
    // 为了降低 BigDecimal 反序列化性能损耗以及语言差异，使用String进行序列化传递
    private String qty;

}
