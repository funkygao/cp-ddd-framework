package org.cp.oms.infra.translator;

import org.x.cp.ddd.runtime.IBaseTranslator;
import org.cp.oms.domain.model.OrderModel;
import org.cp.oms.infra.po.OrderMainData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface OrderTranslator extends IBaseTranslator<OrderModel, OrderMainData> {

    OrderTranslator instance = Mappers.getMapper(OrderTranslator.class);

    @Override
    OrderMainData translate(OrderModel orderModel);
}
