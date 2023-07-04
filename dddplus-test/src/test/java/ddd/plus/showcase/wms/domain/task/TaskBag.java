package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Platform;
import io.github.dddplus.model.ListBag;
import io.github.dddplus.model.spcification.Notification;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaskBag extends ListBag<Task> {
    protected TaskBag(@NonNull List<Task> items) {
        super(items);
    }

    public static TaskBag of(List<Task> tasks) {
        return new TaskBag(tasks);
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {

    }

    public BigDecimal totalQty() {
        return items.stream().map(Task::totalQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalPendingQty() {
        return items.stream().map(Task::totalPendingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalCheckedQty() {
        return totalQty().subtract(totalPendingQty());
    }

    public List<Platform> platformNos() {
        List<Platform> r = new ArrayList<>();
        for (Task task : items) {
            if (task.getPlatformNo().isPresent()) {
                r.add(task.getPlatformNo());
            }
        }
        return r;
    }

}
