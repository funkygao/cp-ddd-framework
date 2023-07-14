package ddd.plus.showcase.wms.infrastructure.domain.order.association;

import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderCartonsDb implements Order.OrderCartons {
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
    public int totalCartonizedQty() {
        return 0;
    }

}
