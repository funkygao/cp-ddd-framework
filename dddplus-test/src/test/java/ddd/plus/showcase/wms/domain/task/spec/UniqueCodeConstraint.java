package ddd.plus.showcase.wms.domain.task.spec;

import ddd.plus.showcase.wms.domain.common.UniqueCode;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.AllArgsConstructor;

/**
 * 一个任务下的唯一码不能重复.
 */
@AllArgsConstructor
public class UniqueCodeConstraint extends AbstractSpecification<Task> {
    // 复核员扫描得到的货品唯一码
    private UniqueCode uniqueCode;

    @Override
    public boolean isSatisfiedBy(Task task, Notification notification) {
        return !task.cartons().contains(uniqueCode);
    }
}
