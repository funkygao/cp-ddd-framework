package ddd.plus.showcase.wms.domain.common.gateway;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.spec.CartonizationRule;
import io.github.dddplus.model.IGateway;

/**
 * 规则中心RPC的防腐层.
 */
public interface IRuleGateway extends IGateway {
    CartonizationRule fetchCartonizationRule(Carton carton);

}
