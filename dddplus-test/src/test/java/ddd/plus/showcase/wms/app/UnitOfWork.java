package ddd.plus.showcase.wms.app;

import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBagCanceled;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.Task;
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

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull Task task) {
        taskRepository.save(task);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull Task task, @NonNull Order order) {
        taskRepository.save(task);
        orderRepository.save(order);
    }

    public void persist(@NonNull Task task, @NonNull OrderBagCanceled canceledBag) {
        orderRepository.switchToCanceledStatus(canceledBag);
        taskRepository.save(task);
    }

}
