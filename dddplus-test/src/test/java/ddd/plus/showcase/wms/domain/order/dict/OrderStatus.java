package ddd.plus.showcase.wms.domain.order.dict;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    /**
     * 正常.
     */
    Normal(10),
    /**
     * 已被客户取消.
     */
    Canceled(100),

    /**
     * 仓内无法生产，退回给上游OFC.
     */
    Bounced(120);

    int value;

    public boolean isNormal() {
        return Normal.equals(this);
    }

}
