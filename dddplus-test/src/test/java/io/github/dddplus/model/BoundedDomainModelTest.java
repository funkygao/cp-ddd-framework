package io.github.dddplus.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class BoundedDomainModelTest {

    @Test
    public void demo() {
        Order order = new Order();
        order.orderNo = "SO-123";
        order.hello();
        OrderPack orderPack = order.inContextOfPack();
        orderPack.packNo = "PO-89";
        orderPack.pack();
        assertSame(order, orderPack.unbounded());
    }

    @Slf4j
    static class Order implements IUnboundedDomainModel {
        @Getter
        String orderNo;

        public void hello() {
            log.info("orderNo:{}", orderNo);
        }

        public OrderPack inContextOfPack() {
            return new OrderPack(this);
        }
    }

    // 订单在打包上下文的模型
    @Slf4j
    static class OrderPack extends BoundedDomainModel<Order> {
        String packNo;

        OrderPack(Order model) {
            this.model = model;
        }

        public void pack() {
            log.info("orderNo:{} packNo:{}", model.getOrderNo(), packNo);
        }
    }


}