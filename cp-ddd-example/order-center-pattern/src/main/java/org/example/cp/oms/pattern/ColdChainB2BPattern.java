package org.example.cp.oms.pattern;

import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.ext.IIdentityResolver;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Pattern(code = ColdChainB2BPattern.CODE, name = "冷链B2B模式")
public class ColdChainB2BPattern implements IIdentityResolver<IOrderModel> {
    public static final String CODE = "ccb2b";

    @Override
    public boolean match(@NotNull IOrderModel model) {
        return model.isB2B() && model.isColdChain();
    }
}
