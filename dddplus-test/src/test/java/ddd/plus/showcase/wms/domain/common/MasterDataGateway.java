package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.IGateway;

/**
 * 主数据RPC防腐层.
 */
public interface MasterDataGateway extends IGateway {

    boolean allowPerformChecking(Operator operator);
}
