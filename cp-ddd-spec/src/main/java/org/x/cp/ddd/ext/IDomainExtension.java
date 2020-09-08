package org.x.cp.ddd.ext;

/**
 * 领域扩展点接口.
 * <p>
 * <p>ATTENTION: 扩展点方法的返回值，必须是Java类，而不能是int/boolean等primitive types，否则可能抛出NPE</p>
 */
public interface IDomainExtension {
    String DefaultCode = "_default_";
}
