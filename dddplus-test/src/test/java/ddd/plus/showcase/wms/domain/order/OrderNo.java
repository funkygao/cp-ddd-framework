package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class OrderNo extends AbstractBusinessNo<String> {
    private OrderNo(@NonNull String value) {
        super(value);
    }

    public static OrderNo of(@NonNull String orderNo) {
        return new OrderNo(orderNo);
    }

}
