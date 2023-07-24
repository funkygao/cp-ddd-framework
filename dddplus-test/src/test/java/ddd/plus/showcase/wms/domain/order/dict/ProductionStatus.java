package ddd.plus.showcase.wms.domain.order.dict;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProductionStatus {
    TaskAccepted,
    UnderChecking, // 正在复核
    Checked, // 复核完成
    Cartonized, // 已装箱
    Loaded, // 已装车
    Shipping, // 正在发货
    Shipped, // 已发货，订单的终态


}
