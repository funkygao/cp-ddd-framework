package ddd.plus.showcase.wms.app;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.OrderBagCanceled;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.TaskOfContainer;
import ddd.plus.showcase.wms.domain.task.TaskOfSku;
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

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull TaskOfSku task, @NonNull Carton carton) {
        taskRepository.save(task);
        cartonRepository.save(carton);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull TaskOfContainer task, @NonNull OrderBagCanceled canceledBag) {
        // sync with local storage
        orderRepository.switchToCanceledStatus(canceledBag);
        taskRepository.save(task);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persist(@NonNull Carton carton) {
        // 内部会保存好几张表：carton, carton_item, consumable, ...
        cartonRepository.save(carton);
    }

}
