package ddd.plus.showcase.wms.application.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ContainerItemResult implements Serializable {
    private String taskNo;
    private String sku;
    private String skuName;
    private BigDecimal qty;
    private String orderNo;
    private String orderLineNo;
}
