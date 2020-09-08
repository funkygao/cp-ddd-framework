package org.cp.oms.spec.ext;

import org.cp.oms.spec.model.IOrderModel;
import org.x.cp.ddd.model.IDomainExtension;

import javax.validation.constraints.NotNull;

/**
 * 生成、分配订单号扩展点.
 */
public interface IAssignOrderNoExt extends IDomainExtension {

    void assignOrderNo(@NotNull IOrderModel model);

}
