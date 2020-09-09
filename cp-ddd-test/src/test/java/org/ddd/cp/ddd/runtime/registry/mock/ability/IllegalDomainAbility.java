package org.ddd.cp.ddd.runtime.registry.mock.ability;

import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;

import javax.validation.constraints.NotNull;

// @DomainAbility(domain = FooDomain.CODE, name = "foo")
public class IllegalDomainAbility extends BaseDomainAbility<FooModel, IFooExt> {

    public String submit(FooModel model) {
        String s1 = "submit received: " + String.valueOf(this.getExtension(model, null).execute(model));
        return s1 + ", firstExt got: " + String.valueOf(firstExtension(model).execute(model));
    }


    @Override
    public IFooExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
