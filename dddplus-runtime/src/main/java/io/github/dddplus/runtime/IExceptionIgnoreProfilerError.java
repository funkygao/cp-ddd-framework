/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

/**
 * A marker interface applied on Exception：被{@code Governance}拦截的方法如果抛出异常是否降低profiler可用率.
 *
 * <p>示例：{@code MyBusinessException extends RuntimeException implements IExceptionIgnoreProfilerError}</p>
 */
public interface IExceptionIgnoreProfilerError {
}
