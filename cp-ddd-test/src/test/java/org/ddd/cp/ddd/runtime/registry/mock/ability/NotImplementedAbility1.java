package org.ddd.cp.ddd.runtime.registry.mock.ability;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.ext.INotImplementedExt1;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE)
@Slf4j
public class NotImplementedAbility1 extends BaseDomainAbility<FooModel, INotImplementedExt1> {

    public void ping(FooModel model) {
        firstExtension(model).doSth();
    }

    @Override
    public INotImplementedExt1 defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
