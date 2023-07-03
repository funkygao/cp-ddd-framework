package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import io.github.dddplus.model.IRepository;

public interface ICartonRepository extends IRepository {
    Carton mustGet(CartonNo cartonNo, WarehouseNo warehouseNo);
}
