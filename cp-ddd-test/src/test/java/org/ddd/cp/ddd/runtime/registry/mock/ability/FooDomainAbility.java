package org.ddd.cp.ddd.runtime.registry.mock.ability;

import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE, name = "foo")
public class FooDomainAbility extends BaseDomainAbility<FooModel, IFooExt> {

    public String submit(FooModel model) {
        if (model.isWillSleepLong() || model.isWillThrowRuntimeException()) {
            firstExtension(model, 5).execute(model); // 5ms
            return "";
        }

        String s1 = "submit received: " + String.valueOf(this.getExtension(model, null).execute(model));
        return s1 + ", firstExt got: " + String.valueOf(firstExtension(model).execute(model));
    }


    @Override
    public IFooExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
