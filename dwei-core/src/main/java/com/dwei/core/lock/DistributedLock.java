package com.dwei.core.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁接口
 *
 * @author hww
 */
public interface DistributedLock {

    <T> T tryLock(String lockId, long keepMills, Supplier<T> supplier);

    <T> T tryLock(String lockId, long keepMills, long waitTime, Supplier<T> supplier);

    <T> T tryLock(String lockId, long keepMills, long waitTime, TimeUnit waitTimeUnit, Supplier<T> supplier);

}
