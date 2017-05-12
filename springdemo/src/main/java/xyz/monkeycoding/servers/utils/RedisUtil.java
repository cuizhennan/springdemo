package xyz.monkeycoding.servers.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Use
 * @Project springdemo
 * @Author Created by CZN on 2017/5/12.
 */
public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class.getName());

    private static JedisPool jedisPool = null;
    private static ThreadLocal<JedisPool> poolThreadLocal = new ThreadLocal<>();

    private RedisUtil() {
    }

    public static JedisPool getPool() {
        if (jedisPool == null) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.valueOf(PropertiesUtils.getKey("redis.pool.maxTotal")));
            jedisPoolConfig.setMaxIdle(Integer.valueOf(PropertiesUtils.getKey("redis.pool.maxIdle")));
            jedisPoolConfig.setMaxWaitMillis(Long.valueOf(PropertiesUtils.getKey("redis.pool.maxWaitMillis")));
            jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(PropertiesUtils.getKey("redis.pool.testOnBorrow")));
            jedisPoolConfig.setTestOnReturn(Boolean.valueOf(PropertiesUtils.getKey("redis.pool.testOnReturn")));

            jedisPool = new JedisPool(jedisPoolConfig, PropertiesUtils.getKey("redis.ip"),
                    Integer.valueOf(PropertiesUtils.getKey("redis.port")),
                    Integer.valueOf(PropertiesUtils.getKey("redis.pool.timeout")));
        }

        return jedisPool;
    }

    public static JedisPool getConnection() {
        if (poolThreadLocal.get() == null) {
            jedisPool = RedisUtil.getPool();
            poolThreadLocal.set(jedisPool);
            return jedisPool;
        }

        return poolThreadLocal.get();
    }

    /**
     * 返还到连接池
     *
     * @param pool
     * @param jedis
     */
    public static void returnResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {
        byte[] value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 删除数据
     *
     * @param key
     * @return
     */
    public static Long del(String key) {
        Long value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 判断是否存在
     *
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        Boolean value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 赋值数据
     *
     * @param key
     * @param value
     * @param expireSeconds(过期时间，秒)
     * @return value
     */
    public static Long set(String key, String value, int expireSeconds) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            jedis.set(key, value);
            result = jedis.expire(key, expireSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param expireSeconds(过期时间，秒)
     * @return value
     */
    public static Long expire(String key, int expireSeconds) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.expire(key, expireSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 赋值数据
     *
     * @param key
     * @return
     */
    public static String set(String key, String value) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 赋值数据
     *
     * @param key
     * @return
     */
    public static Long sadd(String key, String value) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.sadd(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 判断set中是否有值
     *
     * @param key
     * @return
     */
    public static Boolean sismember(String key, String member) {
        Boolean result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.sismember(key, member);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }
}
