package ddd.plus.showcase.wms.domain.carton;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class CartonNo extends AbstractBusinessNo<String> {
    protected CartonNo(@NonNull String value) {
        super(value);
    }

    public static CartonNo of(@NonNull String cartonNo) {
        return new CartonNo(cartonNo);
    }
}
