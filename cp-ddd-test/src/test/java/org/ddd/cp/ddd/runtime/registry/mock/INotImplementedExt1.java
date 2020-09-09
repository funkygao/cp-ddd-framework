package org.ddd.cp.ddd.runtime.registry.mock;

import org.ddd.cp.ddd.ext.IDomainExtension;

// 故意做成无人实现该扩展点
public interface INotImplementedExt1 extends IDomainExtension {
    void doSth();
}
