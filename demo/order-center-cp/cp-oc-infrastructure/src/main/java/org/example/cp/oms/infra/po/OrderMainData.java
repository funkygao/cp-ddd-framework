package org.example.cp.oms.infra.po;

import lombok.Data;

@Data
public class OrderMainData {
    private Long orderId;

    // 预留字段，中台负责存储，前台负责业务
    private String x1;
    private String x2;
    private String x3;
    private String x4;
    private String x5;
}
