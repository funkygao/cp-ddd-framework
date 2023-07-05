package io.github.design;

import io.github.dddplus.model.DirtyMemento;
import io.github.dddplus.model.Exchange;
import io.github.dddplus.model.IAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder // 使用Builder模式回收setter方法，同时mapstruct/单元测试都可以方便运行
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ShipmentOrder implements IAggregateRoot, IUnboundedDomainModel {
    @Builder.Default
    private DirtyMemento dirtyMemento = new DirtyMemento();
    @Builder.Default
    private Exchange exchange = new Exchange();

    @Getter
    private Long id;
}
