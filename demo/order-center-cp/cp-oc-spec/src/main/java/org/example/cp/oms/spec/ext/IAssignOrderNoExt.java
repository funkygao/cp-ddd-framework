package org.example.cp.oms.spec.ext;

import org.example.cp.oms.spec.model.IOrderModel;
import io.github.dddplus.ext.IDomainExtension;

import javax.validation.constraints.NotNull;

/**
 * 生成、分配订单号扩展点.
 */
public interface IAssignOrderNoExt extends IDomainExtension {

    void assignOrderNo(@NotNull IOrderModel model);

}
