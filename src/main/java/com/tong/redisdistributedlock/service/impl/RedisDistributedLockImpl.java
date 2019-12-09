package com.tong.redisdistributedlock.service.impl;

import com.tong.redisdistributedlock.service.RedisDistributedLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class RedisDistributedLockImpl implements RedisDistributedLockService {

    protected static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static RedisDistributedLockImpl redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    private void init() {
        redisTemplate = this;
        redisTemplate.stringRedisTemplate = this.stringRedisTemplate;
    }

    @Override
    public Boolean getRedisDistributedLock(String key, String value, Integer keyAliveTime, TimeUnit timeUnit) {
        stringRedisTemplate = redisTemplate.stringRedisTemplate;

        /**
         * 1、使用redis的setnx
         * 2、不加try finally 造成死锁，中间单带抛出exception
         *    加try finally 执行过程宕机，也是死锁，为redis设置过期时间
         * 3、加try finally之后，也加过期时间，key如果不是一一对应（一个请求一把锁），
         * 4、阻塞和非阻塞方面考虑
         *    阻塞锁：将用户请求保留，知道获取锁
         *    非阻塞锁：获取不到锁，立即返回
         * 5、业务执行时间过长，超出过期时间。造成锁失效
         *    单独开启线程，异步续命，待实现
         */
        //保证value的线程安全
        /*Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //线程异步续命
                thread = new Thread() {
                    @Override
                    public void run(){
                        stringRedisTemplate.expire(key, 100, TimeUnit.SECONDS);
                    }
                };
            }
        };
        timer.schedule(timerTask, keyAliveTime*1000);*/
        Boolean lock = false;
        THREAD_LOCAL.set(value);
        try {
            //保证锁可重入性，同一个节点的同一个线程如果获取了锁之后，也可以再次获取锁
            if (THREAD_LOCAL.get().equals(stringRedisTemplate.opsForValue().get(key))) {
                lock = true;
            }
            if (THREAD_LOCAL.get() != null) {
                lock = stringRedisTemplate.opsForValue().setIfAbsent(key, value, keyAliveTime, timeUnit);
            }
        } catch (Exception e){
            log.error("设置分布式锁错误");
            System.out.println("分布式锁错误");
        }
        return lock;
    }

    @Override
    public Boolean releaseRedisDistributedLock(String key) {
        stringRedisTemplate = redisTemplate.stringRedisTemplate;
        String value = THREAD_LOCAL.get();
        //String[] strings = value.split(":");
        if (value.equals(stringRedisTemplate.opsForValue().get(key))) {
            Boolean delete = stringRedisTemplate.delete(key);
            //根据线程id获取线程，关闭异步续命线程
            /*Thread thread = findThread(Long.valueOf(strings[1]));
            if (thread != null) {
                thread.stop();
            }*/
        }
        return true;
    }

    /**
     * 通过线程组获得线程
     *
     * @param threadId
     * @return
     */
    public static Thread findThread(long threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadId == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }

}
