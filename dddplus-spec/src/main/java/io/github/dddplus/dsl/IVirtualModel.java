/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

/**
 * 逆向建模时识别出的当前代码里并不存在的业务对象.
 *
 * <p>有了这个业务对象，业务模型更直观表达.</p>
 * <p>用于通过{@link KeyFlow#actor()}来承载业务行为职责.</p>
 */
public interface IVirtualModel {
}
