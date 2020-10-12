package io.github.dddplus.runtime.registry.mock.ability;

import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;

import javax.validation.constraints.NotNull;

@DomainAbility(domain = FooDomain.CODE, name = "foo")
public class FooDomainAbility extends BaseDomainAbility<FooModel, IFooExt> {

    public String submit(FooModel model) {
        if (model.isWillSleepLong() || model.isWillThrowRuntimeException()) {
            firstExtension(model, 500).execute(model); // 500ms
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
