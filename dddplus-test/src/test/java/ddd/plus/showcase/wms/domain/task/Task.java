package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.carton.CartonItem;
import ddd.plus.showcase.wms.domain.carton.CartonItemBag;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBag;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.dict.TaskExchangeKey;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.DirtyMemento;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.HasMany;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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

    public TaskStatus status() {
        return status;
    }

    @KeyElement(types = KeyElement.Type.Location, byType = true)
    Platform platformNo;
    // 该复核任务由哪一个操作员完成：1个任务只能1人完成
    Operator operator;
    @Getter
    private WarehouseNo warehouseNo;

    DirtyMemento dirtyMemento() {
        return this.memento;
    }


    @lombok.experimental.Delegate
    @KeyRelation(whom = ContainerBag.class, type = KeyRelation.Type.HasOne)
    private ContainerBag containerBag;

    public void injectContainerBag(@NonNull Class<? extends ITaskRepository> _any, ContainerBag containerBag) {
        this.containerBag = containerBag;
    }

    // ============
    // associations
    // ============

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

    @lombok.experimental.Delegate
    private TaskOrders orders;

    /**
     * Note：确保不滥用，只能{@link ITaskRepository}才能调用
     */
    public void injectOrders(@NonNull Class<? extends ITaskRepository> _any, TaskOrders orders) {
        this.orders = orders;
    }

    public interface TaskCartonItems extends HasMany<CartonItem> {
        /**
         * 该任务已经装箱的货品明细.
         */
        CartonItemBag cartonItemBag();
    }

    @lombok.experimental.Delegate
    private TaskCartonItems cartonItems;

    public void injectCartonItems(@NonNull Class<? extends ITaskRepository> _any, TaskCartonItems cartonItems) {
        this.cartonItems = cartonItems;
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    @KeyBehavior
    public void claimedWith(Operator operator, Platform platformNo) {
        this.platformNo = platformNo;
        this.operator = operator;
        mergeDirty(new TaskDirtyHint(this).dirty("operator", "platform_no"));
    }

}
