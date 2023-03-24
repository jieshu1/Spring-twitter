package com.jie.twitter.config;
import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheClientFactory;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.spring.SSMCache;
import com.google.code.ssm.spring.SSMCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;


@Configuration
@EnableCaching
@PropertySource(value = {"classpath:application.properties"})
public class CacheConfig extends CachingConfigurerSupport {

    private Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    /*
    // JedisPoolFactory can perform direct connection to Redis and operations like below:
    //Jedis jedisClient = jedisPool.getResource();
    //jedisClient.set("key1","value1");
    //jedisClient.close();
    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout);

        logger.info("JedisPool is successfully registered");
        logger.info("redis address：" + host + ":" + port);
        return  jedisPool;
    }
    */

    // Old version of RedisCacheManager config. New version doesn't accept RedisTemplate anymore.
    /*

     @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        // Defaults
        redisConnectionFactory.setHostName("127.0.0.1");
        redisConnectionFactory.setPort(6379);
        redisConnectionFactory.setTimeout(600);
        redisConnectionFactory.setUsePool(true);
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }

    @Bean(name="cacheManagerRedis")
    public CacheManager cacheManagerRedis(RedisTemplate redisTemplate) {
       RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

    // Number of seconds before expiration. Defaults to unlimited (0)
       cacheManager.setDefaultExpiration(300);
       return cacheManager;
    }


     */

    // Set up two CacheManagers, primary is using memcached, and second is with Redis


    @Primary
    @Bean(name = "cacheManager")
    public CacheManager cacheManager(Cache memCache){
        SSMCacheManager cacheManager = new SSMCacheManager();
        SSMCache ssmCache = new SSMCache(memCache, 600, false);
        Collection<SSMCache> caches = new HashSet<>();
        caches.add(ssmCache);
        cacheManager.setCaches(caches);
        return cacheManager;

    }

    @Bean(name = "memCache")
    public CacheFactory memCache(){
        CacheClientFactory cacheClientFactory = new MemcacheClientFactoryImpl();
        DefaultAddressProvider addressProvider = new DefaultAddressProvider("127.0.0.1:11211");
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setConsistentHashing(true);
        CacheFactory cacheFactory = new CacheFactory();
        cacheFactory.setCacheName("memCache");
        cacheFactory.setCacheClientFactory(cacheClientFactory);
        cacheFactory.setAddressProvider(addressProvider);
        cacheFactory.setConfiguration(cacheConfiguration);
        return cacheFactory;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
        return lettuceConnectionFactory;
    }

    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(1))
                .disableKeyPrefix()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        //redisCacheConfiguration.usePrefix();
        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;

    }

    @Bean
    public org.springframework.cache.Cache redisCache(RedisCacheManager redisCacheManager){
        org.springframework.cache.Cache redisCache = redisCacheManager.getCache("redisCache");
        return redisCache;
    }



}
