package ddd.plus.showcase.wms.infra.order.association;

import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.infra.dao.Dao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class OrderCartonsDb implements Order.OrderCartons {
    private final Order order;
    @Autowired
    private Dao dao;

    @Override
    public int totalCartonizedQty() {
        return dao.query("select sum(checked_qty) from ob_carton_item where order_no=? and warehouse_no=?",
                order.getOrderNo().value(),
                order.getWarehouseNo().value());
    }

}
