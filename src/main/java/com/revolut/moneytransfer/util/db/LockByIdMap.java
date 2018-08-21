package com.revolut.moneytransfer.util.db;


import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Lock by id map.
 */
public class LockByIdMap {

    private final ConcurrentHashMap<Long, ReadWriteLock> accLockMap = new ConcurrentHashMap<>();

    /**
     * Get read write lock.
     *
     * @param accountId the account id
     * @return the read write lock
     */
    public ReadWriteLock  get(@NotNull Long accountId) {
        ReadWriteLock lock = accLockMap.get(accountId);
        if (lock == null) {
            synchronized (accLockMap) {
                lock = accLockMap.get(accountId);
                if (lock == null) {
                    lock = new ReentrantReadWriteLock();
                    accLockMap.put(accountId, lock);
                }
            }
        }
        return lock;
    }
}