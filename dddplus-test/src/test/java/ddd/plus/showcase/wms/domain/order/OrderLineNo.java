package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class OrderLineNo extends AbstractBusinessNo<String> {
    private OrderLineNo(@NonNull String value) {
        super(value);
    }

    public static OrderLineNo of(@NonNull String orderLineNo) {
        return new OrderLineNo(orderLineNo);
    }

}
