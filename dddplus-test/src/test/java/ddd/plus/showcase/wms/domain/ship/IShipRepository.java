package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBagCanceled;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.model.IRepository;

public interface IShipRepository extends IRepository {

    void save(ShipManifest shipManifest);

}
