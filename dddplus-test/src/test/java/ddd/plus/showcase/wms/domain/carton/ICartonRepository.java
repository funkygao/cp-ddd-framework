package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.model.IRepository;

public interface ICartonRepository extends IRepository {
    Carton mustGet(CartonNo cartonNo, WarehouseNo warehouseNo) throws WmsException;

    void save(Carton carton);
}
