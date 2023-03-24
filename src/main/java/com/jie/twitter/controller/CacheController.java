package com.jie.twitter.controller;

import com.jie.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CacheController {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisCacheManager redisCacheManager;


    //@Autowired
    //private JedisPool jedisPool;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/twitter/iscached", method = RequestMethod.GET)
    public ResponseEntity<Object> getCachedUserProfile(@RequestBody Map<String, String> params){
        String email = params.get("email").toLowerCase();
        //Jedis jedisClient = jedisPool.getResource();
        //jedisClient.set("key1","value1");
        //jedisClient.close();
        Cache cacheMem = cacheManager.getCache("memCache");
        Cache cacheRedis = redisCacheManager.getCache("redisCache");
        System.out.println("get redis cache:" + cacheRedis);
        if (cacheRedis == null){
            redisCacheManager.initializeCaches();
        }
        cacheRedis.putIfAbsent("testkey", "testvalue");
        Object testredis = cacheRedis.get("testkey");
        System.out.println("test put get from redis cache:" + testredis.toString());

        String key = String.format("userprofile:%s",email);
        Object resultMem = cacheMem.get(key);
        String keyRedis = String.format("tweets:%s",email);
        Object resultRedis = cacheRedis.get(keyRedis);
        System.out.println("cached userprofile:" + resultMem);
        System.out.println("cached tweets:" + resultRedis);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.OK;
        map.put("status", status.value());
        map.put("userprofile is cached", resultMem!=null);
        map.put("tweet list is cached", resultRedis!=null);
        return new ResponseEntity<Object>(map,status);
    }

}
