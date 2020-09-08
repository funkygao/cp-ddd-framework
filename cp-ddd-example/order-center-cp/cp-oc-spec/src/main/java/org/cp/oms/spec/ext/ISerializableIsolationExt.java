package org.cp.oms.spec.ext;

import org.cp.oms.spec.model.IOrderModel;
import org.cp.oms.spec.model.vo.LockEntry;
import org.x.cp.ddd.model.IDomainExtension;

import javax.validation.constraints.NotNull;

/**
 * 订单串行化隔离的扩展点声明.
 */
public interface ISerializableIsolationExt extends IDomainExtension {


    /**
     * 获取防并发锁的信息.
     *
     * @param model
     * @return lock entry object. if null, 不需要防并发
     */
    LockEntry createLockEntry(@NotNull IOrderModel model);

}
