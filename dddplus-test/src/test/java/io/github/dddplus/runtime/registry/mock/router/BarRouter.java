package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Governance;
import io.github.dddplus.annotation.LogInfo;
import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.IReducer;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.extension.B2CExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Router(domain = FooDomain.CODE)
@Slf4j
public class BarRouter extends BaseRouter<IFooExt, FooModel> {
    public static final String EX = "blah";

    @LogInfo(in = true, out = true)
    @Governance
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

    @LogInfo(in = true)
    public void throwsEx(FooModel model) {
        log.info("will throw exception...");
        throw new RuntimeException(EX);
    }

    @Governance
    @LogInfo(inOut = true)
    public String submit2(FooModel model) {
        Integer result = forEachExtension(model, IReducer.allOf(new Predicate<Integer>() {
            @Override
            public boolean test(Integer v) {
                return v != null && v.equals(B2CExt.RESULT);
            }
        })).execute(model);

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
