package ddd.plus.showcase.wms.domain.task.hint;

import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.IDirtyHint;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class RemoveContainerItemsHint implements IDirtyHint {
    private final Task task;
    private final Set<OrderLineNo> orderLineNoList;
}
