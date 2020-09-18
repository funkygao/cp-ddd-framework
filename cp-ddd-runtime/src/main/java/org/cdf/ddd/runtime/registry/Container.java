package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.plugin.IPluginListener;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * 业务容器.
 */
@Slf4j
public class Container {
    private static final Container instance = new Container();

    public static Container getInstance() {
        return instance;
    }

    /**
     * 加载业务前台jar包.
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Exception
     */
    public void loadPartner(@NotNull String jarPath, String basePackage) throws Exception {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("loading partner:{} basePackage:{}", jarPath, basePackage);

        try {
            new PartnerLoader().load(jarPath, basePackage);
        } catch (Exception ex) {
            log.error("load partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }

        // bingo!
        getListener().onLoad(new ContainerContext(DDDBootstrap.applicationContext()));

        log.warn("loaded partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 加载业务模式jar包.
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Exception
     */
    public void loadPattern(@NotNull String jarPath, String basePackage) throws Exception {

    }

    String jarName(String jarPath) {
        int lastSlash = jarPath.lastIndexOf(File.separator);
        if (lastSlash == -1) {
            return jarPath;
        }

        return jarPath.substring(lastSlash + 1);
    }

    private IPluginListener getListener() {
        // FIXME 多个业务jar，会报错
        return DDDBootstrap.applicationContext().getBean(IPluginListener.class);
    }
}
