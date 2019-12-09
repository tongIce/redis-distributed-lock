package com.tong.redisdistributedlock.controller;

import com.tong.redisdistributedlock.service.RedisDistributedLockService;
import com.tong.redisdistributedlock.service.impl.RedisDistributedLockImpl;
import com.tong.redisdistributedlock.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value = "order")
@Slf4j
public class OrderController {

    private static final String STOCK = "stock";

    @Autowired
    private RedisDistributedLockImpl redisDistributedLockService;

    /*@Autowired
    private StringRedisTemplate stringRedisTemplate;*/
    /*@Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("order")
    public Boolean order(@RequestBody OrderVo orderVo) {
        Boolean result = false;
        try {
            String string = UUID.randomUUID().toString();
            TimeUnit seconds = TimeUnit.SECONDS;
            Boolean redisDistributedLock = redisDistributedLockService.getRedisDistributedLock(orderVo.getUserId().toString(), string,
                    300, TimeUnit.SECONDS);
            if (!redisDistributedLock) {
                return false;
            }
            String stock = stringRedisTemplate.opsForValue().get(STOCK);
            Integer count = Integer.valueOf(stock);
            if (count > 0) {
                // todo something
                System.out.println("当前库粗剩余为：" + count);
                stringRedisTemplate.opsForValue().decrement(STOCK);
                result = true;
            } else {
                System.out.println("库存被清空");
                result = false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            redisDistributedLockService.releaseRedisDistributedLock(orderVo.getUserId().toString());
        }
        return result;
    }*/

    @RequestMapping("application")
    public String springBoot() {
        /*RedisDistributedLockImpl redisDistributedLockService = new RedisDistributedLockImpl();
        Boolean lock = redisDistributedLockService.getRedisDistributedLock("daidifj", "60"
                , 100, TimeUnit.SECONDS);*/
        Boolean lock = redisDistributedLockService.getRedisDistributedLock("daidifj", "60"
                , 100, TimeUnit.SECONDS);
        /*Boolean lock = RedisDistributedLockImpl.redisTemplate.getRedisDistributedLock("daidifj", "60"
                , 100, TimeUnit.SECONDS);*/
        System.out.println("加锁的结果是："+lock);
        return "success";
    }

}
