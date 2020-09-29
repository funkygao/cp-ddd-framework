package org.example.cp.oms.domain.facade.lock;

import org.example.cp.oms.domain.facade.cache.IRedisClient;
import org.example.cp.oms.spec.model.vo.LockEntry;

import java.util.concurrent.locks.Lock;

public interface IRedisLockFactory {

    /**
     * Create a mutex lock.
     */
    Lock create(IRedisClient redisClient, LockEntry lockEntry);
}
