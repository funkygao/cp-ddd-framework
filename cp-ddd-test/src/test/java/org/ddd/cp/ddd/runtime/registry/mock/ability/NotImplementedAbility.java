package org.ddd.cp.ddd.runtime.registry.mock.ability;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.INotImplementedExt;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE)
@Slf4j
public class NotImplementedAbility extends BaseDomainAbility<FooModel, INotImplementedExt> {

    public void ping(FooModel model) {
        firstExtension(model).doSth();
    }

    @Override
    public INotImplementedExt defaultExtension(@NotNull FooModel model) {
        return () -> log.info("blah blah");
    }
}
