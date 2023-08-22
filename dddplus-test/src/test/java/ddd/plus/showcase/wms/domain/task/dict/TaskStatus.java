package ddd.plus.showcase.wms.domain.task.dict;

import com.google.common.collect.Lists;
import io.github.dddplus.dsl.KeyElement;

import java.util.List;

/**
 * 任务的生命周期.
 */
public enum TaskStatus {
    // 已接收
    @KeyElement(remarkFromJavadoc = true)
    Accepted,
    @KeyElement
    Claimed,
    @KeyElement
    Finished,
    @KeyElement
    Appending;

    public static List<TaskStatus> allowCheckStatus() {
        return Lists.newArrayList(Accepted, Claimed);
    }

    /**
     * 是否可以执行复核作业.
     */
    public boolean canPerformChecking() {
        return allowCheckStatus().contains(this);
    }
}
