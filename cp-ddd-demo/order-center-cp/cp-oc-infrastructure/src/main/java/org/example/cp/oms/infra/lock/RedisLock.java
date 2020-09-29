package org.example.cp.oms.infra.lock;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.facade.cache.IRedisClient;
import org.example.cp.oms.spec.model.vo.LockEntry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
class RedisLock implements Lock {

    private final IRedisClient redisClient;
    private final LockEntry lockEntry;

    RedisLock(IRedisClient redisClient, LockEntry lockEntry) {
        this.redisClient = redisClient;
        this.lockEntry = lockEntry;
    }

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        log.info("这里通过 IRedisClient 实现分布式锁, {}", lockEntry);
        return tryLock(lockEntry.getLeaseTime(), lockEntry.getTimeUnit());
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return true;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
