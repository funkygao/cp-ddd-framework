package ddd.plus.showcase.wms.domain.task.dict;

import com.google.common.collect.Lists;

import java.util.List;

public enum TaskStatus {
    Submitted,
    Ongoing,
    Finished;

    private static List<TaskStatus> allowCheckStatus() {
        return Lists.newArrayList(Submitted, Ongoing);
    }

    /**
     * 是否可以执行复核作业.
     */
    public boolean canPerformChecking() {
        return allowCheckStatus().contains(this);
    }
}
