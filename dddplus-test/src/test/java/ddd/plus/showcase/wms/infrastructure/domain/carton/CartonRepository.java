package ddd.plus.showcase.wms.infrastructure.domain.carton;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonBag;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.carton.hint.CartonDirtyHint;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.common.gateway.IInventoryGateway;
import ddd.plus.showcase.wms.domain.common.gateway.IRuleGateway;
import ddd.plus.showcase.wms.domain.common.publisher.IEventPublisher;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import ddd.plus.showcase.wms.infrastructure.domain.carton.association.CartonOrderDb;
import ddd.plus.showcase.wms.infrastructure.domain.carton.association.CartonPalletDb;
import ddd.plus.showcase.wms.infrastructure.domain.carton.convert.CartonConverter;
import io.github.dddplus.dsl.KeyBehavior;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CartonRepository implements ICartonRepository {
    private static final CartonConverter converter = CartonConverter.INSTANCE;

    @Resource
    private Dao dao;

    @Resource
    private IRuleGateway ruleGateway;
    @Resource
    private IEventPublisher kafkaProducer;
    @Resource
    private IInventoryGateway inventoryGateway;

    @Override
    public Carton mustGet(CartonNo cartonNo, WarehouseNo warehouseNo) throws WmsException {
        CartonPo po = dao.query(""); // eager load with items
        Carton carton = converter.fromPo(po);
        carton.injects(new CartonOrderDb(carton, dao), new CartonPalletDb(carton.getPalletNo(), dao),
                ruleGateway, inventoryGateway, kafkaProducer);
        return carton;
    }

    @Override
    public CartonBag mustGet(OrderNo orderNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    /**
     * 可合并的hint如何合并数据库请求
     */
    @Override
    @KeyBehavior
    public void save(Carton carton) {
        CartonPo cartonPo = new CartonPo(); // 此时所有字段为空
        CartonDirtyHint hint = carton.firstHintOf(CartonDirtyHint.class);
        if (hint.has(CartonDirtyHint.Type.BindOrder)) {
            // update ob_carton
            cartonPo.setOrderNo(carton.getOrderNo().value());
            cartonPo.setCheckedQty(hint.getCheckedQty());
        }

        if (hint.has(CartonDirtyHint.Type.Fulfill)) {
            cartonPo.setCartonStatus(carton.getStatus());
            cartonPo.setPlatformNo(carton.getPlatform().value());
            cartonPo.setOperatorNo(carton.getOperator().value());
            cartonPo.setFulfillTime(carton.getFulfillTime());
            // (BindOrder, Fulfill) => update ob_carton
            dao.update(cartonPo);
        }

        if (hint.has(CartonDirtyHint.Type.FromContainer)) {
            // insert ob_carton_item
            List<CartonItemPo> cartonItemPoList = converter.toCartonItemPo(carton.items());
            dao.insert(cartonItemPoList);
        }

        if (hint.has(CartonDirtyHint.Type.InstallConsumables)) {
            // insert ob_carton_consumable
            List<ConsumablePo> consumablePoList = CartonConverter.INSTANCE.toConsumablePo(carton.getConsumableBag().items());
            dao.insert(consumablePoList);
        }
    }

    /**
     * Bag of root如何持久化
     */
    @Override
    @KeyBehavior
    public void save(CartonBag cartonBag) {
        cartonBag.items().forEach(c -> this.save(c));
    }
}
