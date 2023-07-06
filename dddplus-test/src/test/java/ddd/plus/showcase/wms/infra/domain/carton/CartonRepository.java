package ddd.plus.showcase.wms.infra.domain.carton;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.common.gateway.IRuleGateway;
import ddd.plus.showcase.wms.infra.dao.Dao;
import ddd.plus.showcase.wms.infra.domain.carton.association.CartonOrderDb;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CartonRepository implements ICartonRepository {
    private static Class _self = CartonRepository.class;

    @Resource
    private Dao dao;

    @Resource
    private IRuleGateway ruleGateway;

    @Override
    public Carton mustGet(CartonNo cartonNo, WarehouseNo warehouseNo) throws WmsException {
        Carton carton = dao.query("");
        carton.injectRuleGateway(_self, ruleGateway);
        carton.injectCartonOrder(_self, new CartonOrderDb(carton, dao));

        return null;
    }

    @Override
    public void save(Carton carton) {

    }
}
