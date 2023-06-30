package ddd.plus.showcase.wms.app;

import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskRepository;
import io.github.dddplus.model.IUnitOfWork;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Setter(onMethod_ = {@Autowired})
@Slf4j
public class UnitOfWork implements IUnitOfWork {
    private TaskRepository taskRepository;

    @Transactional(rollbackFor = Exception.class)
    public void persist(Task task) {
        taskRepository.save(task);
    }


}
