package org.example.cp.oms.domain.model;

import io.github.dddplus.api.RequestProfile;
import io.github.dddplus.model.IDomainModelCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderModelCreator implements IDomainModelCreator {

    private RequestProfile requestProfile;

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
