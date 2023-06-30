package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.IBag;

import java.util.List;

public class OrderLineBag implements IBag {
    private List<OrderLine> orderLines;
}
