package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.task.dict.TaskStatus;
import ddd.plus.showcase.wms.domain.task.hint.TaskDirtyHint;
import io.github.dddplus.buddy.DirtyMemento;
import io.github.dddplus.buddy.Exchange;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
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
@KeyRelation(whom = Container.class, type = KeyRelation.Type.HasMany)
public class Task extends BaseAggregateRoot<Task> implements IUnboundedDomainModel {
    @Builder.Default
    private DirtyMemento memento = new DirtyMemento();
    @Builder.Default
    private Exchange exchange = new Exchange();

    private Long id;
    private Integer priority;

    @KeyElement(types = KeyElement.Type.DCU)
    private TaskNo taskNo;
    @KeyElement(types = KeyElement.Type.Lifecycle)
    private TaskStatus status;
    @KeyElement(types = KeyElement.Type.Location)
    private PlatformNo platformNo;
    /**
     * 该复核任务由哪一个操作员完成：1个任务只能1人完成.
     */
    private Operator operator;
    private WarehouseNo warehouseNo;

    private ContainerBag containerBag;

    public int totalSku() {
        return containerBag.totalSku();
    }

    public BigDecimal totalQty() {
        return containerBag.totalQty();
    }

    public void bind(Operator operator, PlatformNo platformNo) {
        this.platformNo = platformNo;
        this.operator = operator;
        memento.register(new TaskDirtyHint(this).dirty("operator", "platform_no"));
    }

}
