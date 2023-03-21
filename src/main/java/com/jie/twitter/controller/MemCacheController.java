package com.jie.twitter.controller;

import com.jie.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
public class MemCacheController {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/twitter/isuserprofilecached", method = RequestMethod.GET)
    public ResponseEntity<Object> getCachedUserProfile(@RequestBody Map<String, String> params){
        String email = params.get("email").toLowerCase();
        //Jedis jedis = jedisPool.getResource();
        //jedis.set("key1","value1");
        Cache cache = cacheManager.getCache("memCache");
        String key = String.format("userprofile:%s",email);
        System.out.println("cached:" + cache.get(key));
        Object result = cache.get(key);
        Map<String, Object> map = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.OK;
        map.put("status", status.value());
        map.put("userprofile is cached", result!=null);
        return new ResponseEntity<Object>(map,status);
    }

}
