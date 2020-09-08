package org.x.cp.ddd.model;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.x.cp.ddd.ext.IDomainExtension;
import org.x.cp.ddd.runtime.IReducer;
import org.x.cp.ddd.runtime.registry.ExtensionDef;
import org.x.cp.ddd.runtime.registry.InternalIndexer;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 扩展点的动态代理.
 *
 * @param <Ext> 扩展点
 * @param <R>   扩展点方法的返回值类型
 */
@Slf4j
class ExtensionInvocationHandler<Ext extends IDomainExtension, R> implements InvocationHandler {
    private static ExecutorService extInvokeTimerExecutor = new ThreadPoolExecutor(
            10,
            System.getProperty("invokeExtMaxPoolSize") != null ? Integer.valueOf(System.getProperty("invokeExtMaxPoolSize")) : 50,
            5L, TimeUnit.MINUTES, // 线程5m内idle，则被回收
            new SynchronousQueue<>(), // 无队列，线程池满后新请求进来则 RejectedExecutionException
            new NamedThreadFactory("ExtInvokeTimer", false)); // daemon=false, shutdown时等待扩展点执行完毕

    private final Class<Ext> extInterface;
    private final IDomainModel model;
    private final IReducer<R> reducer;
    private final Ext defaultExt;
    private int timeoutInMs;

    ExtensionInvocationHandler(@NotNull Class<Ext> extInterface, @NotNull IDomainModel model, IReducer<R> reducer, Ext defaultExt, int timeoutInMs) {
        this.extInterface = extInterface;
        this.model = model;
        this.reducer = reducer;
        this.defaultExt = defaultExt;
        this.timeoutInMs = timeoutInMs;
    }

    Ext createProxy() {
        return (Ext) Proxy.newProxyInstance(extInterface.getClassLoader(), new Class[]{this.extInterface}, this);
    }

    @Override
    public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {
        List<ExtensionDef> effectiveExts = InternalIndexer.findEffectiveExtensions(extInterface, model, reducer == null);
        log.debug("{} effective {}", extInterface.getCanonicalName(), effectiveExts);

        if (effectiveExts.isEmpty()) {
            if (defaultExt == null) {
                log.debug("found NO ext instance {} on {}, HAS TO return null", extInterface.getCanonicalName(), model);
                // 扩展点方法的返回值不能是int/boolean等，否则会抛出NPE!
                return null;
            }

            log.debug("use default {}", defaultExt);
            effectiveExts.add(new ExtensionDef(defaultExt));
        }

        // all effective extension instances found
        List<R> accumulatedResults = new ArrayList<>(effectiveExts.size());
        R result = null;
        for (ExtensionDef extensionDef : effectiveExts) {
            result = invokeExtension(extensionDef, method, args);
            accumulatedResults.add(result);

            if (reducer == null || reducer.shouldStop(accumulatedResults)) {
                break;
            }
        }

        if (reducer != null) {
            // reducer决定最终的返回值
            return this.reducer.reduce(accumulatedResults);
        }

        // 没有reducer，那么返回最后一个扩展点的执行结果
        return result;
    }

    private R invokeExtension(ExtensionDef extensionDef, final Method method, Object[] args) throws Throwable {
        try {
            return invokeExtensionMethod(extensionDef, method, args);
        } catch (InvocationTargetException e) {
            // 此处接收被调用方法内部未被捕获的异常：扩展点里抛出异常
            log.error("{} code:{}", this.extInterface.getCanonicalName(), extensionDef.getCode(), e.getTargetException());
            throw e.getTargetException();
        } catch (TimeoutException e) {
            log.error("timed out:{}ms, {} method:{} args:{}", timeoutInMs, extensionDef.getExtensionBean(), method.getName(), args);
            // java里的TimeoutException继承Exception，需要转为ExtTimeoutException，否则上层看到的异常是 UndeclaredThrowableException
            throw new ExtTimeoutException(timeoutInMs);
        } catch (RejectedExecutionException e) {
            log.error("ExtInvokeTimer thread pool FULL:{}", e.getMessage()); // 需要加日志报警
            throw e;
        } catch (Throwable e) {
            log.error("{} code:{} unexpected", this.extInterface.getCanonicalName(), extensionDef.getCode(), e);
            throw e;
        }
    }

    private R invokeExtensionMethod(ExtensionDef extensionDef, Method method, Object[] args) throws Throwable {
        IDomainExtension extInstance = extensionDef.getExtensionBean();
        if (timeoutInMs > 0) {
            // JSF扩展点超时使用JSF自带的，没必要浪费线程池资源
            // 即：cp-core的扩展点超时与JSF超时不叠加使用
            return invokeExtensionMethodWithTimeout(extInstance, method, args, timeoutInMs);
        }

        R result = (R) method.invoke(extInstance, args);
        log.debug("{} method:{} args:{}, result:{}", extInstance, method.getName(), args, result);

        return result;
    }

    private R invokeExtensionMethodWithTimeout(IDomainExtension extInstance, Method method, Object[] args, final int timeoutInMs) throws Throwable {
        // 切换到线程池ThreadLocal会失效，目前ThreadLocal只有MDC
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        Future<R> future = extInvokeTimerExecutor.submit(() -> {
            MDC.setContextMap(mdcContext); // 手动继承前面线程的MDC
            try {
                return (R) method.invoke(extInstance, args);
            } finally {
                MDC.clear();
            }
        });

        try {
            R result = future.get(timeoutInMs, TimeUnit.MILLISECONDS);
            log.debug("{} method:{} args:{}, result:{}", extInstance, method.getName(), args, result);

            return result;
        } catch (TimeoutException e) {
            if (!future.isCancelled()) {
                future.cancel(true); // best effort
            }

            throw e;
        } catch (ExecutionException e) {
            // future的异常机制，这里尽可能把真实的异常抛出去
            throw e.getCause() != null ? e.getCause() : e;
        }
    }

}
