package io.github.design;

import io.github.dddplus.model.DirtyMemento;
import io.github.dddplus.model.Exchange;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.HasMany;
import io.github.dddplus.model.association.HasOne;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The check task.
 */
@KeyRelation(whom = CheckBasicRule.class, type = KeyRelation.Type.HasMany)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CheckTask implements IAggregateRoot, IUnboundedDomainModel {
    @Builder.Default
    private DirtyMemento dirtyMemento = new DirtyMemento();
    @Builder.Default
    private Exchange exchange = new Exchange();

    @Getter
    private Long id;
    @Getter
    private String taskNo;

    private StationNo stationNo;
    private HasOne<Operator> operator;
    private Details details;
    private ShipmentOrders orders;

    @Autowired
    private CheckTaskDomainService checkTaskDomainService;

    /**
     * here we go for status.
     */
    @KeyElement(types = KeyElement.Type.Lifecycle, name = "theStatus", byType = true)
    private CheckTaskStatus status;

    @KeyElement(types = KeyElement.Type.Lifecycle)
    private Integer printStatus;

    @KeyElement(types = {KeyElement.Type.Referential, KeyElement.Type.Structural})
    private String containerNo;

    @KeyElement(types = KeyElement.Type.Location)
    private String locationNo;

    @KeyElement(types = KeyElement.Type.Referential)
    private HasMany<ShipmentOrder> shipmentOrders;

    @KeyBehavior(name = "复核", remark = "ok")
    void foo() {
    }

    @Deprecated
    @KeyElement(types = KeyElement.Type.Contextual)
    private int foo;

    private CheckTask(Essence essence) {
        // 这里进行进行各种规约检查，确保业务状态完整性和一致性
        this.status = CheckTaskStatus.Accepted;

        // 这里也可以使用mapstruct
        this.locationNo = essence.locationNo;
        this.containerNo = essence.containerNo;
    }

    @KeyRule(name = "isDone")
    boolean isFinished() {
        return false;
    }

    @KeyBehavior(produceEvent = CheckTaskFinished.class, async = true)
    public void finish() {

    }

    public Operator operator() {
        return operator.get();
    }


    public interface Details extends HasMany<CheckTaskDetail> {
        List<CheckTaskDetail> listBy(ContainerNo containerNo);
    }

    public interface ShipmentOrders extends HasMany<ShipmentOrder> {
        List<ShipmentOrder> pendingOrders();
    }

    public String helo() {
        return checkTaskDomainService.helo();
    }

    /**
     * {@link CheckTask}DTO形式的替身，用于创建任务的场景(此时数据库里还没有记录)，贫血，只有数据无行为和逻辑.
     */
    @Data
    public static class Essence {
        private String taskNo;
        private String containerNo;
        private String locationNo;
        public CheckTask createCheckTask() {
            return new CheckTask(this);
        }
    }
}
