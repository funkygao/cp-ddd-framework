package org.x.cp.ddd.runtime.registry;

import org.x.cp.ddd.annotation.Extension;
import org.x.cp.ddd.annotation.Partner;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务前台jar包动态加载器.
 * <p>
 * <p>对某一个{@link Partner}的所有扩展点实例通过单独的{@link ClassLoader}进行加载，从而实现业务前台隔离.</p>
 * <p>如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.</p>
 */
@Slf4j
public class PartnerLoader {
    private final String jarPath;

    private PartnerClassLoader partnerClassLoader;

    /**
     * Constructor of project container.
     *
     * @param jarPath project jar jarPath
     */
    public PartnerLoader(@NotNull String jarPath) {
        this.jarPath = jarPath;
    }

    /**
     * 加载业务前台模块.
     *
     * @throws Exception
     */
    public void load() throws Exception {
        long t0 = System.nanoTime();
        log.info("loading with {}", label());

        List<Class<? extends Annotation>> annotations = new ArrayList<>(2);
        // 只加载业务前台的扩展点和Partner注解类，通过java ClassLoader的全盘负责机制自动加载相关引用类
        annotations.add(Partner.class);
        annotations.add(Extension.class);

        initPartnerClassLoaderIfNec();

        Map<Class<? extends Annotation>, List<Class>> resultMap = JarUtils.loadClassWithAnnotations(this.jarPath,
                annotations, null, this.partnerClassLoader);

        // 实例化该业务前台的所有扩展点，并注册到索引
        this.registerPartner(resultMap.get(Partner.class).get(0)); // 1个业务前台包必须且只能有1个Partner注解的类
        this.registerExtensions(resultMap.get(Extension.class));

        log.info("loaded with {} ok, cost {}ms", label(), (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 重新加载业务前台模块.
     *
     * @throws Exception
     */
    private void reload() throws Exception {
        // TODO
    }

    /**
     * 卸载业务前台模块.
     *
     * @throws Exception
     */
    void unload() throws Exception {
        if (this.partnerClassLoader == null) {
            log.warn("{} not loaded yet! Really want to unload it?", label());
            return;
        }

        log.info("unloading {}", label());
        this.partnerClassLoader.close();
        log.info("unloaded {} ok", label());
    }

    private void initPartnerClassLoaderIfNec() throws Exception {
        if (this.partnerClassLoader != null) {
            return;
        }

        synchronized (this) {
            if (this.partnerClassLoader != null) {
                return;
            }

            this.partnerClassLoader = new PartnerClassLoader(new URL[]{new File(this.jarPath).toURI().toURL()});
            log.info("PartnerClassLoader created");
        }
    }

    private void registerExtensions(List<Class> extensions) throws Exception {
        if (extensions == null || extensions.isEmpty()) {
            log.warn("Empty extensions found on {}", this.label());
            return;
        }

        for (Class extensionClazz : extensions) {
            RegistryFactory.lazyRegister(Extension.class, extensionClazz.newInstance());
        }
    }

    private void registerPartner(@NotNull Class project) throws Exception {
        RegistryFactory.lazyRegister(Partner.class, project.newInstance());
    }

    public String label() {
        return "PartnerLoader(jarPath=" + this.jarPath + ")";
    }
}
