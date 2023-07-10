/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

/**
 * A marker interface applied on Exception：扩展点执行中抛出异常时进行弱日志打印.
 *
 * <p>扩展点执行中如果抛出该异常，则日志打印log.warn且不打印异常栈，否则log.error且打印异常栈</p>
 * <p>示例：{@code MyBusinessException extends RuntimeException implements IExceptionWeakLogging}</p>
 */
public interface IExceptionWeakLogging {
}
