package org.example.cp.oms.domain.model;

import org.ddd.cp.ddd.model.IDomainModelCreator;
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
}
