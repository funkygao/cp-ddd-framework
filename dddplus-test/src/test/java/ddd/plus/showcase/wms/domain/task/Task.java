package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import ddd.plus.showcase.wms.domain.task.hint.ConfirmQtyHint;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import io.github.dddplus.buddy.IDirtyHint;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IUnboundedDomainModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@EqualsAndHashCode(exclude = "memento")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter
@KeyRelation(whom = ContainerBag.class, type = KeyRelation.Type.HasOne)
public class Task extends BaseAggregateRoot<Task> implements IUnboundedDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.Operational)
    private Integer priority;
    @KeyElement(types = KeyElement.Type.DCU)
    private TaskNo taskNo;
    @KeyElement(types = KeyElement.Type.Lifecycle)
    private TaskStatus status;
    @KeyElement(types = KeyElement.Type.Location)
    private PlatformNo platformNo;
    // 该复核任务由哪一个操作员完成：1个任务只能1人完成
    private Operator operator;
    private WarehouseNo warehouseNo;

    private ContainerBag containerBag;

    public <T extends IDirtyHint> T firstHintOf(Class<T> hintClass) {
        return memento.firstHintOf(hintClass); // FIXME dup code
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
    public ContainerItemBag pendingItems() {
        return null;
    }

    @KeyBehavior
    public void bind(Operator operator, PlatformNo platformNo) {
        this.platformNo = platformNo;
        this.operator = operator;
        dirty(new TaskDirtyHint(this).dirty("operator", "platform_no"));
    }

    @KeyBehavior(async = false)
    public void confirmQty(BigDecimal qty, Operator operator, PlatformNo platformNo) {
        this.platformNo = platformNo;
        this.operator = operator;
        containerBag.pendingItemBag().allocatedQty(qty);
        dirty(new ConfirmQtyHint(this));
    }



}
