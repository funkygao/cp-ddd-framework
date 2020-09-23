package org.example.bp.oms.isv.extension;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Extension;
import org.example.bp.oms.isv.IsvPartner;
import org.example.cp.oms.spec.ext.IPresortExt;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Extension(code = IsvPartner.CODE, value = "isvPresort")
@Slf4j
public class PresortExt implements IPresortExt {

    @Override
    public void presort(@NotNull IOrderModel model) {
        log.info("ISV里预分拣的结果：{}", new MockInnerClass().getResult());
    }

    private static class MockInnerClass {
        int getResult() {
            return 1;
        }
    }
}
