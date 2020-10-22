/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

/**
 * 可以被动态加载的插件.
 * <p>
 * <pre>
 *            IPlugable
 *               |
 *    +---------------------+
 *    |                     |
 * IIdentityResolver    IDomainExtension
 * </pre>
 */
public interface IPlugable {
}
