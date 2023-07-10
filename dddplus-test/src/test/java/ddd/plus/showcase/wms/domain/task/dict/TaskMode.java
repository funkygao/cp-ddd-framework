package ddd.plus.showcase.wms.domain.task.dict;

/**
 * 作业生产模式.
 */
public enum TaskMode {
    LabelPicking,
    ;

    public boolean isLabelPicking() {
        return LabelPicking.equals(this);
    }
}
