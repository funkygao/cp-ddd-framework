package io.github.design.mybatis;

import io.github.design.CheckTask;
import io.github.design.CheckTaskDetail;
import io.github.design.ShipmentOrder;

import java.util.List;

//@org.apache.ibatis.annotations.Mapper
// with DefaultObjectFactory
public interface ModelMapper {

    CheckTask findCheckTaskById(Long id);

    List<ShipmentOrder> findPendingOrdersByTask(String taskNo);

    List<CheckTaskDetail> findCheckTaskDetailsByContainer(String container);
}
