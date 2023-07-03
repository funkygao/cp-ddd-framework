package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.Sku;
import io.github.dddplus.model.IDomainModel;

public class CartonItem implements IDomainModel {
    private Long id;
    private Sku sku;
}
