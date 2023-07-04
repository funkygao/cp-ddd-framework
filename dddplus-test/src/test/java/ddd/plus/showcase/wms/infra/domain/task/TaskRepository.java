package ddd.plus.showcase.wms.infra.domain.task;

import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.*;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import ddd.plus.showcase.wms.infra.dao.Dao;
import ddd.plus.showcase.wms.infra.domain.task.association.TaskCartonItemsDb;
import ddd.plus.showcase.wms.infra.domain.task.association.TaskOrdersDb;
import ddd.plus.showcase.wms.infra.domain.task.convert.TaskConverter;
import io.github.dddplus.model.Exchange;
import io.github.dddplus.model.IDirtyHint;
import io.github.dddplus.model.IDomainModel;
import io.github.design.ContainerNo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository implements ITaskRepository {
    @Resource
    private Dao dao;

    /**
     * 这里演示如何把数据库里的数据转换为充血模型的{@link IDomainModel}：
     *
     * <ol>
     * <li>通过{@code MyBatis的TypeHandler}进行数据库与特定Java类型的转换</li>
     * <li>通过{@link Exchange} 在基础设施层(put，get)那些(业务无关，技术相关)的数据</li>
     * <li>如何通过{@code mapstruct}进行{@code po -> entity}转换</li>
     * <li>如何把关联对象实例传递给{@link IDomainModel}</li>
     * <li>如何解决{@code (domain, infrastructure)}的root package不同，因此需要开放{@code Setter}方法但又不被误用的问题</li>
     * </ol>
     */
    @Override
    public Task mustGetPending(TaskNo taskNo, WarehouseNo warehouseNo) throws WmsException {
        TaskPo po = dao.query("select inner join .. where warehouse_no=?",
                warehouseNo.value());
        // 1. po -> entity
        Task task = TaskConverter.INSTANCE.toTask(po);

        // 2. 传递技术细节但业务无关的字段到 domain entity
        task.xSet(task.OptimisticLock, po.getVersion());

        // 3. 组装 bag 对象，但不希望被误用
        List<Container> containers = dao.query("select xxx");
        task.injectContainerBag(this.getClass(), ContainerBag.of(containers));

        // 4. 组装 association implementation
        task.injectCartonItems(this.getClass(), new TaskCartonItemsDb(task, dao));
        task.injectOrders(this.getClass(), new TaskOrdersDb(task, dao));

        return task;
    }

    /**
     * 这里演示：
     *
     * <ol>
     * <li>如何通过{@link IDirtyHint}获取落库的线索：trace the dirty data</li>
     * <li>如何在基础设施层进行数据库交互的合并</li>
     * <li>如何通过{@link Exchange}实现领域层无感的技术细节：乐观锁</li>
     * </ol>
     */
    @Override
    public void save(TaskOfSku task) {
        // 1. 获取所有hint，这样才知道该如何落库
        ConfirmQtyHint confirmQtyHint = task.unbounded().firstHintOf(ConfirmQtyHint.class);
        TaskDirtyHint taskDirtyHint = task.unbounded().firstHintOf(TaskDirtyHint.class);

        // 2. 这2个hint可以在Task这个维度合并，以降低数据库交互，从而提升性能

        // 3. DDD里的更新绝大部分采用乐观锁：通过 Exchange 有效传递而不污染领域层
        Short oldVersion = task.unbounded().xGet(Task.OptimisticLock, Short.class);
        dao.execute("update set xxx where yyy and version=?",
                oldVersion);
    }

    @Override
    public TaskOfContainer mustGetPending(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public TaskOfSku mustGetPending(TaskNo taskNo, OrderNo orderNo, Sku sku, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public Map<Platform, List<Task>> pendingTasksOfPlatforms(List<Platform> platformNos) {
        return null;
    }

    @Override
    public TaskBag tasksOfOrder(OrderNo orderNo, WarehouseNo warehouseNo) {
        return null;
    }

    @Override
    public void save(TaskOfContainer task) {
    }
}
