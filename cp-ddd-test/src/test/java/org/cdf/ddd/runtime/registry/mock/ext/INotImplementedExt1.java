package org.cdf.ddd.runtime.registry.mock.ext;

import org.cdf.ddd.ext.IDomainExtension;

// 故意做成无人实现该扩展点
public interface INotImplementedExt1 extends IDomainExtension {
    void doSth();
}
