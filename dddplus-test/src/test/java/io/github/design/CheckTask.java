package io.github.design;

import io.github.dddplus.buddy.DirtyMemento;
import io.github.dddplus.buddy.Exchange;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.association.HasMany;
import io.github.dddplus.model.association.HasOne;
import io.github.dddplus.model.association.Ref;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The check task.
 */
@KeyRelation(whom = ShipmentOrder.class, type = KeyRelation.Type.Many2Many)
@KeyRelation(whom = CheckBasicRule.class, type = KeyRelation.Type.HasMany)
@Builder
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

    private Ref<String> stationNo;
    private HasOne<Operator> operator;
    private Details details;
    private ShipmentOrders orders;

    /**
     * here we go for status.
     */
    @KeyElement(types = KeyElement.Type.Lifecycle, name = "theStatus")
    private String status;

    @KeyElement(types = KeyElement.Type.Lifecycle)
    private Integer printStatus;

    @KeyElement(types = {KeyElement.Type.Referential, KeyElement.Type.Structural})
    private String containerNo;

    @KeyElement(types = KeyElement.Type.Propagational)
    private String locationNo;

    @KeyBehavior(rules = {CheckBasicRule.class, CheckAdvancedRule.class}, modes = "x", modeClass = FooMode.class, name = "复核", remark = "ok")
    void foo() {
    }

    @Deprecated
    @KeyElement(types = KeyElement.Type.Contextual)
    private int foo;

    @KeyRule(name = "isDone")
    boolean isFinished() {
        return false;
    }

    @KeyBehavior(produceEvent = CheckTaskFinished.class)
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
}
