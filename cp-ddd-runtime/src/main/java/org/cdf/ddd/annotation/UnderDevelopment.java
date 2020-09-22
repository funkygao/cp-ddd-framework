package org.cdf.ddd.annotation;

import java.lang.annotation.*;

/**
 * 正在开发中，不要在生产环境使用！
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface UnderDevelopment {
}
