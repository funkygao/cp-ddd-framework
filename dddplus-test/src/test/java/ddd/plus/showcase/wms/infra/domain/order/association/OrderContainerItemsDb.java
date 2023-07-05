package ddd.plus.showcase.wms.infra.domain.order.association;

import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.task.ContainerItemBag;
import ddd.plus.showcase.wms.infra.dao.Dao;
import ddd.plus.showcase.wms.infra.domain.task.ContainerItemPo;
import ddd.plus.showcase.wms.infra.domain.task.convert.TaskConverter;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OrderContainerItemsDb implements Order.OrderContainerItems {
    /**
     * Repository加载时通过构造器注入进来
     */
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private final Order order;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private final Dao dao;

    /**
     * 订单通过关联对象访问另外一个聚合内对象.
     */
    @KeyBehavior
    @Override
    public ContainerItemBag containerItemBag() {
        List<ContainerItemPo> containerItemPoList = dao.listContainerItems(order.getOrderNo().value(), order.getWarehouseNo().value());
        return ContainerItemBag.of(TaskConverter.INSTANCE.fromContainerItemPoList(containerItemPoList));
    }
}
