package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

@Router
public class FooRouter extends BaseRouter<IFooExt, FooModel> {

    public String submit(FooModel model) {
        if (model.isWillSleepLong() || model.isWillThrowRuntimeException()) {
            firstExtension(model, 500).execute(model); // 500ms
            return "";
        }

        String s1 = "submit received: " + String.valueOf(this.forEachExtension(model).execute(model));
        return s1 + ", firstExt got: " + String.valueOf(firstExtension(model).execute(model));
    }


    @Override
    public IFooExt defaultExtension(@NonNull FooModel model) {
        return null;
    }
}
