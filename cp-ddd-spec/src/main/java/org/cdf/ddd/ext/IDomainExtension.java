/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.ext;

/**
 * 扩展点：业务语义确定，但执行逻辑不同的业务功能点，即以不变应万变.
 * <p>
 * <p>ATTENTION: 扩展点方法的返回值，必须是Java类，而不能是int/boolean等primitive types，否则可能抛出NPE</p>
 */
public interface IDomainExtension extends IPlugable {
    String DefaultCode = "_default__";
}
