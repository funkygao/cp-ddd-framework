package org.example.cp.oms.domain.model;

import org.cdf.ddd.model.IDomainModelCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderModelCreator implements IDomainModelCreator {

    /**
     * 订单来源
     */
    private String source;

    /**
     * 客户编号.
     */
    private String customerNo;

    /**
     * 客户携带的外部单号.
     */
    private String externalNo;
}
