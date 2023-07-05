package ddd.plus.showcase.wms.domain.task.hint;

import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.IDirtyHint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConfirmQtyHint implements IDirtyHint {
    private final Task task;
}
