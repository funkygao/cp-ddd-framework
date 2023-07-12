package ddd.plus.showcase.wms.application.worker.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 拣货下发给复核任务的数据.
 */
@Data
public class SubmitTaskDto implements Serializable {
    // 幂等使用
    @NotBlank(message = "{uuid.notBlank}")
    private String uuid;
    // 生产波次
    private String waveNo;
    private String warehouseNo;

    @NotNull(message = "{containerList.notNull}")
    @Size(max = 200)
    private List<ContainerDto> containers;

    private Map<String, Object> extInfo;

    public static class ContainerDto implements Serializable {
        @NotNull(message = "{containerNo.notNull}")
        private String containerNo;
        private Integer containerType;
        private List<ContainerItemDto> containerItems;
    }

    public static class ContainerItemDto implements Serializable {
        private String pickingTaskNo;

        private String orderNo;
        private String orderLineNo;

        private String sku;
        private String skuName;
        private String lotNo;
        private String packCode;

        // Optional: 该货品的序列号，例如，sku茅台有2瓶，每瓶的序列号不同
        private List<String> snList;

        /**
         * 实际拣货数量.
         */
        @DecimalMin(value = "0.000001")
        private BigDecimal pickedQty;

        /**
         * 应该复核数量.
         */
        @DecimalMin(value = "0.000001")
        private BigDecimal checkQty;
    }
}
