package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 复核台(操作台)编号.
 *
 * <p>对于一个规模大的仓库，复核台数量可能300+</p>
 */
public class PlatformNo extends AbstractBusinessNo<String> {
    private PlatformNo(@NonNull String value) {
        super(value);
    }

    public static PlatformNo of(@NonNull String platformNo) {
        return new PlatformNo(platformNo);
    }

}
