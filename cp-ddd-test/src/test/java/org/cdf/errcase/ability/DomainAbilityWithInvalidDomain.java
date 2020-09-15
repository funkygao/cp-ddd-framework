package org.cdf.errcase.ability;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.DomainAbility;
import org.cdf.ddd.model.BaseDomainAbility;
import org.cdf.ddd.runtime.Reducer;
import org.cdf.ddd.runtime.registry.mock.ext.IFooExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.function.Predicate;

@DomainAbility(domain = "non-exist")
@Slf4j
public class DomainAbilityWithInvalidDomain extends BaseDomainAbility<FooModel, IFooExt> {

    public String submit(FooModel model) {
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 1;
            }
        };
        int result = getExtension(model, Reducer.firstOf(predicate)).execute(model);
        return String.valueOf(result);
    }

    public void throwsEx(FooModel model) {
        log.info("will throw exception...");
        throw new RuntimeException("blah");
    }

    public String submit2(FooModel model) {
        Integer result = getExtension(model, Reducer.all()).execute(model);
        if (result == null) {
            return null;
        }

        return String.valueOf(result);
    }

    @Override
    public IFooExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
