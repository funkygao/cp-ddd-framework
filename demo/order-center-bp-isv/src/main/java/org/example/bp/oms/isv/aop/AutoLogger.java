package org.example.bp.oms.isv.aop;

import java.lang.annotation.*;

/**
 * 自动打印入参、出参.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLogger {
}
