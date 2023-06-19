package io.github.errcase.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.IReducer;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Router(domain = "non-exist")
@Slf4j
public class RouterWithInvalidDomain extends BaseRouter<IFooExt, FooModel> {

    public String submit(FooModel model) {
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 1;
            }
        };
        int result = forEachExtension(model, IReducer.stopOnFirstMatch(predicate)).execute(model);
        return String.valueOf(result);
    }

    public void throwsEx(FooModel model) {
        log.info("will throw exception...");
        throw new RuntimeException("blah");
    }

    public String submit2(FooModel model) {
        Integer result = forEachExtension(model, IReducer.allOf(null)).execute(model);
        if (result == null) {
            return null;
        }

        return String.valueOf(result);
    }

    @Override
    public IFooExt defaultExtension(@NonNull FooModel model) {
        return null;
    }
}
