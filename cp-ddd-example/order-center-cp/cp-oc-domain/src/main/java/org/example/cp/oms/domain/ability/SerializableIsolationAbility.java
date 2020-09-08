package org.example.cp.oms.domain.ability;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;
import org.ddd.cp.ddd.annotation.DomainAbility;
import org.ddd.cp.ddd.model.BaseDomainAbility;

import javax.validation.constraints.NotNull;
import java.util.concurrent.locks.Lock;

@DomainAbility(domain = CoreDomain.CODE, name = "订单串行化隔离的能力")
@Slf4j
public class SerializableIsolationAbility extends BaseDomainAbility<IOrderModel, ISerializableIsolationExt> {
    private static final Lock withoutLock = null;

    public Lock acquireLock(@NotNull IOrderModel model) {
        LockEntry lockEntry = firstExtension(model).createLockEntry(model);
        if (lockEntry == null) {
            return withoutLock;
        }

        // 为了避免前台锁key冲突，中台统一加锁前缀，隔离不同的业务前台
        lockEntry.withPrefix(model.getCustomerNo());
        log.debug("key:{} expires:{} {}", lockEntry.lockKey(), lockEntry.getLeaseTime(), lockEntry.getTimeUnit());
        return null;
    }

    public static boolean useLock(Lock lock) {
        return lock != withoutLock;
    }

    @Override
    public ISerializableIsolationExt defaultExtension(@NotNull IOrderModel model) {
        // 默认不防并发
        return null;
    }
}
