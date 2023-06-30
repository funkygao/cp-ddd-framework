package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 复核台(操作台)编号.
 */
public class PlatformNo extends AbstractBusinessNo<String> {
    private PlatformNo(@NonNull String value) {
        super(value);
    }

    public static PlatformNo of(@NonNull String taskNo) {
        return new PlatformNo(taskNo);
    }

}
