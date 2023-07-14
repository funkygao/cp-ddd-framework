package ddd.plus.showcase.wms.infrastructure.domain.task;

import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.*;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import ddd.plus.showcase.wms.infrastructure.domain.task.association.TaskCartonsDb;
import ddd.plus.showcase.wms.infrastructure.domain.task.association.TaskOrdersDb;
import ddd.plus.showcase.wms.infrastructure.domain.task.convert.TaskConverter;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
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
    @KeyElement(types = KeyElement.Type.Structural, remark = "如何克服Java访问权限控制粒度问题")
    private static final TaskConverter converter = TaskConverter.INSTANCE;

    @Resource
    private Dao dao;

    /**
     * 如何把数据库里的数据转换为充血模型的{@link IDomainModel}，并注入关联对象
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
    @KeyBehavior
    public Task mustGet(TaskNo taskNo, WarehouseNo warehouseNo) throws WmsException {
        TaskPo po = dao.query("select inner join .. where warehouse_no=?",
                warehouseNo.value());
        if (po == null) {
            // this is why we name must
            throw new WmsException(WmsException.Code.TaskNotFound);
        }
        // 1. po -> entity
        Task task = converter.fromPo(po);

        // 2. 传递技术细节但业务无关的字段到 domain entity；落库时再取出来
        task.xSet(task.OptimisticLock, po.getVersion());

        // 3. 组装 bag 对象，但不希望被误用
        List<ContainerPo> containerPos = dao.query("select xxx");

        // 4. 实例化并注入 association implementation
        task.injects(ContainerBag.of(converter.fromContainerPoList(containerPos)),
                new TaskCartonsDb(task, dao),
                new TaskOrdersDb(task, dao));
        return task;
    }

    /**
     * 如何把充血模型落库，dirty hint，乐观锁
     *
     * <ol>
     * <li>如何通过{@link IDirtyHint}获取落库的线索：trace the dirty data</li>
     * <li>如何在基础设施层进行数据库交互的合并</li>
     * <li>如何解决为了(查询，报表)而进行的领域层无关数据冗余问题</li>
     * <li>如何通过{@link Exchange}实现领域层无感的技术细节：乐观锁</li>
     * </ol>
     */
    @Override
    @KeyBehavior(useRawArgs = true)
    public void save(Task task) {
        // 1. 获取所有hint，这样才知道该如何落库
        ConfirmQtyHint confirmQtyHint = task.firstHintOf(ConfirmQtyHint.class);
        TaskDirtyHint taskDirtyHint = task.firstHintOf(TaskDirtyHint.class);
        taskDirtyHint.getDirtyFields();

        // 2. 这2个hint可以在Task这个维度合并，以降低数据库交互，从而提升性能

        // 3. 转换为po，为了(查询，报表)进行的字段冗余：denormalization
        TaskPo po = converter.toPo(task);

        // 4. DDD里的更新绝大部分采用乐观锁：通过 Exchange 有效传递而不污染领域层
        po.setVersion(task.xGet(Task.OptimisticLock, Short.class));
        dao.update(po);
    }

    /**
     * 如何使用mapstruct
     */
    @Override
    @KeyBehavior(useRawArgs = true)
    public void save(TaskOfContainerPending taskOfContainerPending) {
        TaskPo po = converter.toPo(taskOfContainerPending.unbounded());
    }

    @Override
    @KeyBehavior(useRawArgs = true)
    public void save(TaskOfOrderPending task) {

    }

    @Override
    public void insert(Task task) {
        TaskPo taskPo = converter.toPo(task);
        // 通过mapstruct把container表里冗余的task字段传递过去
        List<ContainerPo> containerPoList = converter.toContainerPoList(task, task.containerBag().items());
        List<ContainerItemPo> containerItemPoList = converter.toContainerItemPoList(task,
                task.containerBag().flatItems());
        dao.insert(taskPo);
        dao.insert(containerPoList);
        dao.insert(containerItemPoList);
    }

    /**
     * 如何从数据库加载场景对象
     */
    @Override
    @KeyBehavior
    public TaskOfContainerPending mustGet(ContainerNo containerNo, WarehouseNo warehouseNo) throws WmsException {
        ContainerPo containerPo = dao.query("select * from ob_container inner join ob_container_item");
        if (containerNo == null) {
            // this is why we name must
            throw new WmsException(WmsException.Code.ContainerNotFound);
        }

        Container container = converter.fromPo(containerPo);
        TaskPo taskPo = dao.query("");
        Task task = converter.fromPo(taskPo);
        TaskOfContainerPending taskOfContainerPending = new TaskOfContainerPending(task, container);
        return taskOfContainerPending;
    }

    @Override
    public Task mustGet(TaskNo taskNo, OrderNo orderNo, Sku sku, WarehouseNo warehouseNo) throws WmsException {
        Task task = dao.query("");
        task.inSkuPendingScenario(orderNo, sku);
        return task;
    }

    @Override
    public TaskOfOrderPending mustGet(OrderNo orderNo, WarehouseNo warehouseNo) throws WmsException {
        return null;
    }

    @Override
    public Map<Platform, List<Task>> pendingTasksOfPlatforms(List<Platform> platformNos) {
        return null;
    }
}
