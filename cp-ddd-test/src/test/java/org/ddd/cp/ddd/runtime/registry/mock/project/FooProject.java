package org.ddd.cp.ddd.runtime.registry.mock.project;


import org.ddd.cp.ddd.annotation.Partner;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Partner(code = FooProject.CODE, name = "BP::foo")
public class FooProject implements IIdentityResolver<FooModel> {
    public static final String CODE = "jdl.cn.ka";

    @Override
    public boolean match(@NotNull FooModel model) {
        return FooProject.CODE.equals(model.getProjectCode());
    }
}
