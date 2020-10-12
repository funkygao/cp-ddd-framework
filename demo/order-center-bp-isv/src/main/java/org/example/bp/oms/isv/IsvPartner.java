package org.example.bp.oms.isv;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.spec.model.IOrderModel;
import io.github.dddplus.annotation.Partner;
import io.github.dddplus.ext.IIdentityResolver;
import org.springframework.beans.factory.DisposableBean;

@Partner(code = IsvPartner.CODE, name = "ISV业务前台")
@Slf4j
public class IsvPartner implements IIdentityResolver<IOrderModel>, DisposableBean {
    public static final String CODE = "ISV";

    public IsvPartner() {
        // hook how Spring create bean instance
        log.info("ISV new instanced, cl:{}", this.getClass().getClassLoader());
    }

    @Override
    public boolean match(IOrderModel model) {
        if (model.getSource() == null) {
            return false;
        }

        return model.getSource().equalsIgnoreCase(CODE);
    }

    @Override
    public void destroy() throws Exception {
        log.warn("destroy");
    }
}
