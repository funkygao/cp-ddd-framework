package io.github.dddplus.runtime.registry.mock.ext;

import io.github.dddplus.ext.IDomainExtension;

// 故意做成无人实现该扩展点
public interface INotImplementedExt extends IDomainExtension {
    void doSth();
}
