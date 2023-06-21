package io.github.dddplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IBagTest {

    @Data
    public static class Order implements IDomainModel {
        String order;
        OrderLineBag orderLineBag;
    }

    @AllArgsConstructor
    static class OrderLine {
        String sku;
        Integer qty;
    }

    static class OrderLineBag implements IBag {
        private final List<OrderLine> orderLineList;

        private OrderLineBag(List<OrderLine> orderLineList) {
            this.orderLineList = orderLineList;
        }

        public static OrderLineBag of(List<OrderLine> orderLineList) {
            return new OrderLineBag(orderLineList);
        }

        public Integer totalQty() {
            Integer total = 0;
            for (OrderLine orderLine : orderLineList) {
                total += orderLine.qty;
            }

            return total;
        }
    }

    static class OrderBag implements IBag {
        List<Order> orderList;
    }

    @Test
    public void totalQty() {
        OrderLine line1 = new OrderLine("SKU1", 5);
        OrderLine line2 = new OrderLine("SKU2", 8);
        OrderLineBag bag = OrderLineBag.of(Arrays.asList(line1, line2));
        assertEquals(bag.totalQty().intValue(), 13);
    }

}