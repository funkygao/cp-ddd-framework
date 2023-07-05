package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 货品的包装代码.
 */
public class PackCode extends AbstractBusinessNo<String> {
    protected PackCode(@NonNull String value) {
        super(value);
    }

    public static PackCode of(@NonNull String packCode) {
        return new PackCode(packCode);
    }
}
