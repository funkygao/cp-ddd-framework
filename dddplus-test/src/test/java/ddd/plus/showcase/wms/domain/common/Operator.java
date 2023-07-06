package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 操作员.
 */
public class Operator extends AbstractBusinessNo<String> {
    protected Operator(@NonNull String value) {
        super(value);
    }

    public static Operator of(@NonNull String operatorNo) {
        return new Operator(operatorNo);
    }
}
