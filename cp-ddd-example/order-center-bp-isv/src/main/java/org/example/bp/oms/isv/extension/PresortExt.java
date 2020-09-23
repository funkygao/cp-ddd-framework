package org.example.bp.oms.isv.extension;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
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

        // 演示第三方包的使用：guava
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("a");
        multiset.add("a");
        multiset.add("b");
        log.info("count(a): {}", multiset.count("a"));
    }

    // 演示内部类的使用
    private static class MockInnerClass {
        int getResult() {
            return 1;
        }
    }
}
