package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Platform;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 一些复核任务.
 */
public class TaskBag extends ListBag<Task> {
    protected TaskBag(@NonNull List<Task> items) {
        super(items);
    }

    public static TaskBag of(List<Task> tasks) {
        return new TaskBag(tasks);
    }

    /**
     * 总要货量.
     */
    public BigDecimal totalQty() {
        return items.stream()
                .map(Task::containerBag)
                .map(ContainerBag::totalQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 待复核货品总数
     */
    @KeyRule
    public BigDecimal totalPendingQty() {
        return items.stream()
                .map(Task::containerBag)
                .map(ContainerBag::totalPendingQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 已复核货品总数
     */
    @KeyRule
    public BigDecimal totalCheckedQty() {
        return totalQty().subtract(totalPendingQty());
    }

    /**
     * 这些复核任务在哪些复核台.
     */
    @KeyRule
    public List<Platform> platforms() {
        List<Platform> r = new ArrayList<>();
        for (Task task : items) {
            if (task.getPlatform().isPresent()) {
                r.add(task.getPlatform());
            }
        }
        return r;
    }

}
