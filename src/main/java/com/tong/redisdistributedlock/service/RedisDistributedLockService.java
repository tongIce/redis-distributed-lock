package com.tong.redisdistributedlock.service;

import java.util.concurrent.TimeUnit;


public interface RedisDistributedLockService {

    /**
     * 获取redis分布式锁
     * 非阻塞锁实现方式
     *
     * @param key
     * @param value
     * @param keyAliveTime
     * @param timeUnit
     * @return
     */
    public Boolean getRedisDistributedLock(String key, String value, Integer keyAliveTime, TimeUnit timeUnit);

    /**
     * 释放redis分布式锁
     *
     * @param key
     * @return
     */
    public Boolean releaseRedisDistributedLock(String key);

}
