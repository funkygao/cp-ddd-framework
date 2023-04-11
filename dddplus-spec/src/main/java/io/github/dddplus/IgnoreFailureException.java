/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus;

/**
 * 可用率不受影响的异常标记.
 *
 * <p>业务异常实现{@link IgnoreFailureException}后，运行时抛出业务异常，则DDDplus在监控时不降低可用率</p>
 */
public interface IgnoreFailureException {
}
