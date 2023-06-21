package io.github.dddplus.runtime.policy;

import io.github.dddplus.ext.IDomainExtension;

public interface IConsumableExt extends IDomainExtension {

    String recommend(String sku);
}
