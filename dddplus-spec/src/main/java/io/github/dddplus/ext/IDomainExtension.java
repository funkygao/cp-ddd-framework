/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

/**
 * 扩展点：业务语义确定，但执行逻辑不同的业务功能点，即以不变应万变.业务上，代表业务变化点.
 * <p>
 * <p>通过扩展点这个接口，实现业务的多态.</p>
 * <p>ATTENTION: 扩展点方法的返回值，必须是Java类，而不能是int/boolean等primitive types，否则可能抛出NPE!</p>
 * <p>Extensions provide a mechanism for extending the underlying logic of a service. </p>
 * <p>This makes it so that extending teams can implement extension logic in an interface-driven way without modifying the core code of the underlying platform.</p>
 */
public interface IDomainExtension extends IPlugable {
    String DefaultCode = "_default__";
}
