package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.common.gateway.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.common.publisher.IEventPublisher;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBag;
import ddd.plus.showcase.wms.domain.order.OrderLineNo;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.dict.TaskExchangeKey;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import ddd.plus.showcase.wms.domain.task.event.TaskAcceptedEvent;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.DirtyMemento;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.HasMany;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 复核任务.
 */
@EqualsAndHashCode(exclude = "memento")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
public class Task extends BaseAggregateRoot<Task> implements IUnboundedDomainModel, TaskExchangeKey {
    @Getter
    private Long id;

    @Getter
    private TaskNo taskNo;
    private TaskMode taskMode;
    private Integer priority;
    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private TaskStatus status;

    @KeyElement(types = KeyElement.Type.Location, byType = true)
    Platform platform;
    // 1个任务只能1人完成
    Operator operator;
    @Getter
    private WarehouseNo warehouseNo;

    public void allocateTaskNo(Class who, @NonNull TaskNo taskNo) {
        this.taskNo = taskNo;
    }

    /**
     * 初始化时指定复核生产计划
     */
    @KeyBehavior
    public void plan() {
        // 各种打标，把各种业务字段转换为各个作业动作的直接指令
    }

    @KeyRelation(whom = ContainerBag.class, type = KeyRelation.Type.HasOne)
    private ContainerBag containerBag;

    @KeyRelation(whom = TaskOrders.class, type = KeyRelation.Type.Associate)
    private TaskOrders orders;

    @KeyRelation(whom = TaskCartons.class, type = KeyRelation.Type.Associate)
    private TaskCartons cartons;

    @KeyBehavior
    public void claimedWith(Operator operator, Platform platformNo) {
        this.platform = platformNo;
        this.operator = operator;
        mergeDirtyWith(new TaskDirtyHint(this).dirty("operator", "platform_no"));
    }

    @KeyBehavior
    public void removeOrderLines(Set<OrderLineNo> orderLineNos) {

    }

    public TaskMode mode() {
        return taskMode;
    }

    public boolean isEmpty() {
        return containerBag.isEmpty();
    }

    private Set<String> skuNoSet() {
        return containerBag.items().stream()
                .flatMap(container -> container.skuNoSet().stream())
                .collect(Collectors.toSet());
    }

    /**
     * 补全货品信息，例如供货商、类目、品牌等
     */
    public void enrichSkuInfo(IMasterDataGateway gateway) {
        Set<String> skuSet = skuNoSet();
        List<Sku> skuList = gateway.pullSkuBySkuNos(skuSet);
        containerBag.enrichSkuInfo(skuList);
    }

    public void accept(IEventPublisher publisher) {
        this.assureSatisfied(null); // composition of specifications
        this.status = TaskStatus.Accepted;

        TaskAcceptedEvent event = new TaskAcceptedEvent();
        event.setTaskNo(taskNo.value());
        event.setWarehouseNo(warehouseNo.value());
        publisher.publish(event);
    }

    @KeyRule
    public int totalSku() {
        return containerBag.totalSku();
    }

    @KeyRule
    public BigDecimal totalQty() {
        return containerBag.totalQty();
    }

    @KeyRule
    public BigDecimal totalPendingQty() {
        return containerBag.totalPendingQty();
    }

    @KeyRule
    public Set<OrderNo> orderNoSet() {
        return containerBag().orderNoSet();
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    public TaskStatus status() {
        return status;
    }

    DirtyMemento dirtyMemento() {
        return this.memento;
    }

    public ContainerBag containerBag() {
        return containerBag;
    }

    public void injectContainerBag(@NonNull Class<ITaskRepository> __, ContainerBag containerBag) {
        this.containerBag = containerBag;
    }

    /**
     * 针对关联关系显式建模.
     *
     * <p>即，关联关系本身也是模型：domain层是接口，infrastructure供应实现.</p>
     */
    public interface TaskOrders extends HasMany<Order> {
        /**
         * 该任务的待复核的所有出库单.
         */
        OrderBag pendingOrders();

        /**
         * 该任务的指定待复核出库单.
         */
        Order pendingOrder(OrderNo orderNo) throws WmsException;
    }

    public TaskOrders orders() {
        return orders;
    }

    public void injectOrders(@NonNull Class<ITaskRepository> __, TaskOrders orders) {
        this.orders = orders;
    }

    public interface TaskCartons extends HasMany<Carton> {
        /**
         * 该任务下所有纸箱里是否已经有该唯一码
         */
        @KeyBehavior(useRawArgs = true)
        boolean contains(@NonNull UniqueCode uniqueCode);
    }

    public TaskCartons cartons() {
        return cartons;
    }

    public void injectCartons(@NonNull Class<ITaskRepository> __, TaskCartons taskCartons) {
        this.cartons = taskCartons;
    }
}
