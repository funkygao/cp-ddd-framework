/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务模式身份解析器的模板方法类.
 * <p>
 * <p>通过模板方法，实现不同领域模型的{@code match}方法动态分发</p>
 * <p>Double Dispatch：determines the method to invoke at runtime based both on the receiver type and the argument types</p>
 * <p>Java语言本身不支持Double Dispatch，通过{@code Visitor Pattern}可以，但太复杂：这里通过反射实现</p>
 * <p>你也可以不使用{@link BasePattern}，直接实现{@link IIdentityResolver}：这是允许的</p>
 * <p>具体的{@link BasePattern}实现类，必须使用{@link Pattern}注解进行标注!</p>
 * <p>IMPORTANT: 子类的每个{@code match}方法入参必须是{@link IIdentity}，研发自己定义的类</p>
 * <pre>
 * {@code
 *
 * ℗Pattern(code = Patterns.Foo)
 * public class FooPattern extends BasePattern {
 *     private boolean match(ShipmentOrder identity) {
 *         return identity.isScenarioOf(new FromFoo());
 *     }
 *     private boolean match(CheckTask identity) {
 *         return identity.getExtInfo("foo", Boolean.class);
 *     }
 *     private boolean match(Carton identity) {
 *         return xxx;
 *     }
 * }
 * }
 * </pre>
 *
 * @see <a href="https://www.baeldung.com/ddd-double-dispatch">Double Dispatch in DDD</a>
 */
@Slf4j
public abstract class BasePattern implements IIdentityResolver<IIdentity> {
    private static final String MATCH_METHOD_NAME = "match";
    // 必须考虑并发场景，因为static，JVM内共享一份cache
    // volatile(double check lock) is not required, since the map object itself will not change
    private static final Map<String, Method> matchMethodCache = new ConcurrentHashMap<>();

    @Override
    public final boolean match(@NonNull IIdentity identity) {
        Class<? extends BasePattern> patternClazz = this.getClass();
        Class<? extends IIdentity> identityClazz = identity.getClass();
        String cacheKey = patternClazz.getName() + ":" + identityClazz.getName(); // pure perf
        Method matchMethod = matchMethodCache.get(cacheKey);
        if (matchMethod == null) {
            log.debug("pattern match method miss: {}", cacheKey);
            try {
                synchronized (BasePattern.class) {
                    // double check to lower first time reflection overhead
                    matchMethod = matchMethodCache.get(cacheKey);
                    if (matchMethod == null) {
                        log.debug("get pattern match method with reflection: {}", cacheKey);
                        matchMethod = getMatchMethod(patternClazz, identityClazz);
                        matchMethodCache.put(cacheKey, matchMethod);
                    } else {
                        log.debug("got pattern after double check: {}", cacheKey);
                    }
                }
            } catch (Exception e) {
                // matchMethod if null, exception will be thrown here
                throw new IllegalArgumentException(patternClazz.getName() + " match method " + e.getClass().getSimpleName() + " " + e.getMessage(), e);
            }
        } else {
            log.debug("pattern match method hit: {}", cacheKey);
        }

        try {
            // matchMethod should never be null
            matchMethod.setAccessible(true);
            log.debug("invoking {}.match({})", patternClazz.getSimpleName(), identityClazz.getSimpleName());
            // 实现类的match方法返回值可以是boolean，也可以Boolean
            boolean matched = (boolean) matchMethod.invoke(this, identity);
            if (matched) {
                log.info("{} Pattern matched for {}", patternClazz.getSimpleName(), identity);
            }
            return matched;
        } catch (InvocationTargetException e) {
            // match方法内抛出异常，无论主动还是被动，JVM会把异常包装在InvocationTargetException.cause中抛出
            Throwable actualException = e.getCause();
            if (actualException != null) {
                // 去掉包装，还原真相
                if (actualException instanceof RuntimeException) {
                    throw (RuntimeException) actualException;
                } else {
                    // 非RuntimeException
                    throw new IllegalStateException(
                            patternClazz.getName()
                                    + ".match("
                                    + identityClazz.getSimpleName()
                                    + ") failed. See cause: "
                                    + actualException.getMessage(),
                            actualException);
                }
            }

            // 什么时候会走到这里？should never happen
            // 1. 被反射调用的方法(match)中没有抛出异常，但是调用该方法的代码使用反射机制将异常包装在InvocationTargetException中抛出
            // 2. match方法抛出了一个空异常或未定义的异常，这种情况很少出现
            throw new IllegalStateException(
                    patternClazz.getName()
                            + ".match("
                            + identityClazz.getSimpleName()
                            + ") weirdly failed. See cause: "
                            + e.getMessage(),
                    e);
        } catch (IllegalAccessException e) {
            // should never happen!
            throw new IllegalStateException(
                    patternClazz.getName()
                            + ".match("
                            + identityClazz.getSimpleName()
                            + ") access failed. See cause: "
                            + e.getMessage(),
                    e);
        }
    }

    private Method getMatchMethod(Class patternClazz, Class modelClazz) throws NoSuchMethodException {
        Method method;
        try {
            // assume protected or private...
            method = patternClazz.getDeclaredMethod(MATCH_METHOD_NAME, modelClazz);
        } catch (Exception e) {
            // then public...
            method = patternClazz.getMethod(MATCH_METHOD_NAME, modelClazz);
        }

        return method;
    }

}
