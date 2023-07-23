package ddd.plus.showcase.wms.application;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonBag;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.common.Uuid;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBagCanceled;
import ddd.plus.showcase.wms.domain.ship.IShipRepository;
import ddd.plus.showcase.wms.domain.ship.ShipManifest;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskOfContainerPending;
import ddd.plus.showcase.wms.domain.task.TaskOfOrderPending;
import io.github.dddplus.model.IUnitOfWork;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Setter(onMethod_ = {@Autowired})
@Slf4j
public class UnitOfWork implements IUnitOfWork {
    private ITaskRepository taskRepository;
    private IOrderRepository orderRepository;
    private ICartonRepository cartonRepository;
    private IShipRepository shipRepository;

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull Task task, @NonNull Carton carton) {
        taskRepository.save(task);
        cartonRepository.save(carton);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull TaskOfOrderPending task, @NonNull Order order, @NonNull CartonBag cartonBag) {
        orderRepository.save(order);
        taskRepository.save(task);
        cartonRepository.save(cartonBag);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull TaskOfContainerPending task, @NonNull OrderBagCanceled canceledBag) {
        // sync with local storage
        orderRepository.switchToCanceledStatus(canceledBag);
        taskRepository.save(task);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull Carton carton) {
        // 内部会保存好几张表：carton, carton_item, consumable, ...
        cartonRepository.save(carton);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull Task task, @NonNull OrderBagCanceled canceledBag, @NonNull Uuid uuid) {
        if (!uuid.assureVaryOnce()) {
            return;
        }

        taskRepository.insert(task);
        orderRepository.switchToCanceledStatus(canceledBag);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull ShipManifest shipManifest, @NonNull Order order) {
        shipRepository.save(shipManifest);
        orderRepository.save(order);
    }

}
