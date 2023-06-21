package io.github.design.mybatis.associations;

import io.github.design.CheckTask;
import io.github.design.ShipmentOrder;
import io.github.design.mybatis.ModelMapper;

import javax.inject.Inject;
import java.util.List;

// mybatis/mapper.xml
// ==================
// <resultMap id="taskOrders" type="io.github.design.mybatis.associations.TaskOrders">
//     <result column="task_no" property="taskNo" javaType="String"/>
// </resultMap>
//
// <resultMap id="checkTask" type="io.github.design.CheckTask">
//     <id column="id" property="id" jdbcType="BIGINT"/>
//     <association property="orders" resultMap="taskOrders"/>
// </resultMap>
// association reference lifecycle implemented here
// connected object graph VS disconnected aggregates
public class TaskOrders implements CheckTask.ShipmentOrders {
    private String taskNo;

    @Inject
    private ModelMapper modelMapper;

    @Override
    public List<ShipmentOrder> pendingOrders() {
        return modelMapper.findPendingOrdersByTask(taskNo);
    }
}
