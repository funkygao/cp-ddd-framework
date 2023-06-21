package io.github.dddplus.annotation;

import java.lang.annotation.*;

/**
 * 自动打印入参出参.
 *
 * <p>日志级别是{@code log.info}.</p>
 * <p>如果被拦截的方法抛出异常，则不输出出参日志了：by design，上层通常会统一处理异常</p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogInfo {
    /**
     * 是否打印入参日志
     */
    boolean in() default false;

    /**
     * 是否打印出参日志
     */
    boolean out() default false;

    /**
     * 是否在一行里输出入参和出参日志.
     *
     * <p>具有最高优先级，如果为{@code true}，则忽略单独的{@link #in()}和{@link #out()}.</p>
     */
    boolean inOut() default false;
}
