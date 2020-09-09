package org.example.cp.oms.infra.translator;

import org.example.cp.oms.infra.po.OrderMainData;
import org.ddd.cp.ddd.IBaseTranslator;
import org.example.cp.oms.domain.model.OrderModel;
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
