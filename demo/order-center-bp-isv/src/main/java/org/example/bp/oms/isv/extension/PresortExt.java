package org.example.bp.oms.isv.extension;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.Extension;
import org.example.bp.oms.isv.IsvPartner;
import org.example.bp.oms.isv.aop.AutoLogger;
import org.example.bp.oms.isv.extension.util.WarehouseUtil;
import org.example.cp.oms.spec.ext.IPresortExt;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Extension(code = IsvPartner.CODE, value = "isvPresort")
@Slf4j
public class PresortExt implements IPresortExt {

    @Override
    @AutoLogger
    public void presort(@NotNull IOrderModel model) {
        log.info("ISV里预分拣的结果：{}", new MockInnerClass().getResult());

        // 演示第三方包的使用：guava
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("a");
        multiset.add("a");
        multiset.add("b");
        log.info("count(a): {}", multiset.count("a"));
        log.info("仓库号：{}", WarehouseUtil.getWarehouseNo());

        // Guava loaded with sun.misc.Launcher$AppClassLoader@1540e19d
        log.info("Guava loaded with {}", multiset.getClass().getClassLoader());

        loadProperties();
    }

    // 演示内部类的使用
    private static class MockInnerClass {
        int getResult() {
            return 1;
        }
    }

    // 演示Plugin自带资源文件的场景
    private void loadProperties() {
        InputStream is = this.getClass().getResourceAsStream("/config.properties");
        InputStreamReader inputStreamReader = null;
        Properties properties = new Properties();
        try {
            inputStreamReader = new InputStreamReader(is, "UTF-8");
            properties.load(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException ignored) {
                }
            }
        }

        log.info("加载资源文件成功！站点名称：{}", properties.getProperty("site_name"));
    }
}
