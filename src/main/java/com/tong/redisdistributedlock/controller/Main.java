package com.tong.redisdistributedlock.controller;

import com.tong.redisdistributedlock.service.RedisDistributedLockService;
import com.tong.redisdistributedlock.service.impl.RedisDistributedLockImpl;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        RedisDistributedLockImpl redisDistributedLockService = new RedisDistributedLockImpl();
        Boolean lock = redisDistributedLockService.getRedisDistributedLock("daidifj", "60"
                , 100, TimeUnit.SECONDS);
        System.out.println("加锁的结果是："+lock);
    }

}
