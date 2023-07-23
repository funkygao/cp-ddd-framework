package ddd.plus.showcase.wms.domain.order.hint;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.model.IDirtyHint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderShippedHint implements IDirtyHint {
    private final Order order;
}
