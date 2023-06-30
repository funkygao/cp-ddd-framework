package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.model.IRepository;

public interface IOrderRepository extends IRepository {

    void save(Order order);

}
