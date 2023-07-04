package ddd.plus.showcase.wms.domain.diff;

import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.diff.dict.DiffReason;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.IAggregateRoot;
import io.github.design.ContainerNo;
import lombok.experimental.Delegate;

/**
 * 复核过程中发现的容器差异.
 */
public class ContainerDiff implements IAggregateRoot {
    @KeyElement(types = KeyElement.Type.Structural)
    private ContainerNo containerNo;
    @KeyElement(types = KeyElement.Type.Location)
    private Platform platform;
    @Delegate
    @KeyElement(types = KeyElement.Type.Operational)
    private DiffReason reason;

    @KeyBehavior
    public void registerBroken() {

    }

    @KeyBehavior
    public void registerLost() {

    }

}
