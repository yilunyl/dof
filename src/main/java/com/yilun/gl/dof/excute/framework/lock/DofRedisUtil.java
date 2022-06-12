package com.yilun.gl.dof.excute.framework.lock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public class DofRedisUtil {
    /**
     * 1 day
     */
    private final static int DEFAULT_CACHE_SEC = 86400;

    public static final long TEN_DAY = 60 * 60 * 24 * 10;
    /**
     * 锁前缀
     */
    private static final String PREFIX = "lock:";
    /**
     * 锁value
     */
    private static final String LOCK_STRING_VALUE = "1";
    private RedisTemplate redisTemplate;

    private DofRedisUtil() {
    }

    public DofRedisUtil(RedisTemplate redisTemplateTemp) {
        redisTemplate = redisTemplateTemp;
    }

    /**
     * 设置，默认过期时间1天
     *
     * @param key   key
     * @param value value
     */
    public void set(String key, Object value) {
        this.set(key, value, DEFAULT_CACHE_SEC, TimeUnit.SECONDS);
    }

    /**
     * 设置，指定过期时间秒
     *
     * @param key     key
     * @param value   value
     * @param timeout timeout 秒
     */
    public void set(String key, Object value, long timeout) {
        this.set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置，指定过期时间
     *
     * @param key     key
     * @param value   value
     * @param timeout timeout
     * @param unit    unit
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 如果key存在返回false；如果key不存在，就存入reidis返回true
     *
     * @param key   key
     * @param value value
     */
    public boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 如果key存在返回false；如果key不存在，就存入reidis返回true
     *
     * @param key   key
     * @param value value
     */
    public boolean setIfAbsent(String key, Object value, long expirationTime, TimeUnit timeUnit) {
        Expiration expiration = Expiration.from(expirationTime, timeUnit);
        return (Boolean) redisTemplate.execute((RedisCallback)
                connection -> connection.stringCommands().set(redisTemplate.getKeySerializer().serialize(key)
                        , redisTemplate.getValueSerializer().serialize(value), expiration, RedisStringCommands.SetOption.SET_IF_ABSENT));
    }


    /**
     * 分布式同步获取
     * <p>可以在多节点 多线程 环境下获取某个key的缓存，若不存在，则同步调用callback，避免缓存击穿 节省数据库或远程API的访问消耗</p>
     *
     * @param key      查找的key
     * @param timeout  expired时长
     * @param lockKey  锁key
     * @param callBack 回调函数
     * @return
     */
    @SuppressWarnings("all")
    public <T> T getWithLock(String key, long timeout, String lockKey, Function<String, T> callBack) throws InterruptedException {

        T value = null;
        try {
            value = (T) get(key);
            if (null == value) {
                DistributedLockUtil.lock(this, lockKey);
                if (null == value) {
                    value = callBack.apply(key);
                    if (timeout > 0) {
                        if (null != value && (value instanceof String && !"".equals(value))) {
                            set(key, value, timeout, TimeUnit.SECONDS);
                        }
                    } else {
                        set(key, value);
                    }

                }
            }
        } catch (InterruptedException e) {
            throw e;
        } finally {
            DistributedLockUtil.unlock(this, lockKey);
        }

        return value;
    }

    /**
     * 分布式同步获取
     * <P>可以在多节点 多线程 环境下获取某个key的缓存，若不存在，则同步调用callback，避免缓存击穿 节省数据库或远程API的访问消耗</P>
     *
     * @param key      查找的key
     * @param lockKey  锁key
     * @param callBack 回调函数
     * @return
     */
    public <T> T getWithLock(String key, String lockKey, Function<String, T> callBack) throws InterruptedException {

        return getWithLock(key, -1, lockKey, callBack);
    }

    /**
     * 分布式获取
     *
     * @param key
     * @param callBack
     * @param <T>
     * @return
     */
    public <T> T getWithLock(String key, Function<String, T> callBack) throws InterruptedException {
        return getWithLock(key, PREFIX + key, callBack);
    }

    public <T> T getWithLock(String key, long timeout, Function<String, T> callBack) throws InterruptedException {
        return getWithLock(key, timeout, PREFIX + key, callBack);
    }


    /**
     * 查询
     *
     * @param key key
     * @return object
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }



    /**
     * 查询
     *
     * @param key key
     * @return object
     */
    public <T> T get(String key, Class<T> type) {
        return (T) redisTemplate.execute((RedisCallback) redisConnection -> {
            byte[] bytes = redisConnection.stringCommands().get(redisTemplate.getKeySerializer().serialize(key));
            GenericJackson2JsonRedisSerializer valueSerializer = (GenericJackson2JsonRedisSerializer) redisTemplate.getValueSerializer();
            return valueSerializer.deserialize(bytes, type);
        });
    }

    /**
     * 删除指定key
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param timeout timeout
     * @return
     */
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param timeout timeout
     * @param unit    unit
     * @return
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 加数
     *
     * @param key   key
     * @param delta delta
     * @return
     */
    public Long increment(String key, Long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 加数
     *
     * @param key   key
     * @param delta delta
     * @return
     */
    public Double increment(String key, Double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Object getAndSet(String key, Object value) {
        return (Object) redisTemplate.opsForValue().getAndSet(key, value);
    }

    public void multiSet(Map<? extends String, ? extends Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    public List<Object> multiGet(Collection<String> collection) {
        return redisTemplate.opsForValue().multiGet(collection);
    }

    //list
    public void addList(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public Object getList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    //Hash
    public void putAll(String key, Map value) {
        redisTemplate.opsForHash().putAll(key, value);
    }

    public Map findAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void put(String key, String field, Object val) {
        redisTemplate.opsForHash().put(key, field, val);
    }

    public Object find(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    //hash批量获取
    public List<Object> hMultiGet(String key, Collection<String> collection) {
        return (List<Object>) redisTemplate.opsForHash().multiGet(key, collection);
    }

    public void remove(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    //SortedSet
    public Boolean zadd(String key, Object val, double score) {
        return redisTemplate.opsForZSet().add(key, val, score);
    }

    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<Object> zRange(String key, long offset) {
        return redisTemplate.opsForZSet().range(key, 0, offset);
    }

    public Set<Object> zRangeByLex(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zRangeByLexWithScore(String key, long min, long max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    public Long zRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }


    public Long zRemove(String key, Object... vals) {
        return redisTemplate.opsForZSet().remove(key, vals);
    }

    public Long zRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public byte[] serialize(Object o) {
        return redisTemplate.getDefaultSerializer().serialize(o);
    }

    public Object deserialize(byte[] bytes) {
        return (Object) redisTemplate.getDefaultSerializer().deserialize(bytes);
    }

    public Set<Object> keys(Object o) {
        return redisTemplate.keys(o);
    }

    public void hset(String key, String field, String value) {
        hset(key, field, value, DEFAULT_CACHE_SEC);
    }

    public void hset(String key, String field, String value, long seconds) {
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);

    }

    public void hset(String key, String field, Object value, long seconds) {
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public void hsetnx(String key, String field, String value) {
        hsetnx(key, field, value, DEFAULT_CACHE_SEC);
    }

    public void hsetnx(String key, String field, String value, long seconds) {
        redisTemplate.opsForHash().putIfAbsent(key, field, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public Long hincr(String key, String field, Long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    public Map<String, String> hgetAll(String key) {
        Map map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    public String hget(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    public boolean hexists(String key, String field) {
        Boolean hexists = redisTemplate.opsForHash().hasKey(key, field);
        return hexists == null ? false : hexists.booleanValue();
    }

    public void hdel(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    public void hdels(String key, String... field) {
        Object[] objs = new Object[field.length];
        System.arraycopy(field, 0, objs, 0, field.length);
        redisTemplate.opsForHash().delete(key, objs);
    }

    public void rpush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public List<Object> range(String key, Long start, Long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void sadd(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long scard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Long srem(String key, Object value) {
        return redisTemplate.opsForSet().remove(key, value);
    }

    public Long srem(String key, List<Object> values) {
        return redisTemplate.opsForSet().remove(key, values.toArray());
    }

    public Long srem(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取分布式锁
     *
     * @param key         锁在redis中的key值
     * @param requestId   请求标识
     * @param expire      锁过期时间
     * @param retryTimes  重试次数
     * @param sleepMillis 每次休息时间
     * @return
     */
    public boolean tryGetDistributedLock(String key, String requestId, long expire, int retryTimes, long sleepMillis) {
        boolean result = tryGetDistributedLock(key, requestId, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = tryGetDistributedLock(key, requestId, expire);
        }
        return result;
    }

    /**
     * 执行脚本
     *
     * @param script
     * @param keys
     * @param args
     * @param <T>
     * @return
     */
    public <T> T execute(RedisScript<T> script, List keys, Object... args) {
        return (T) redisTemplate.execute(script, keys, args);
    }

    /**
     * 执行
     *
     * @param action
     * @param <T>
     * @return
     */
    public <T> T execute(RedisCallback<T> action) {
        return (T) redisTemplate.execute(action);
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取分布式锁
     *
     * @param key       锁在redis中的key值
     * @param requestId 请求标识
     * @param expire    锁过期时间
     * @return
     */
    public boolean tryGetDistributedLock(String key, String requestId, long expire) {
        try {
            String result = (String) redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    return commands.set(key, requestId, "NX", "PX", expire);
                }
            });
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 释放分布式锁
     *
     * @param key
     * @param requestId 请求标识
     * @return
     */
    public boolean releaseDistributedLock(String key, String requestId) {
        //使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
        //spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
        Long result = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, Collections.singletonList(key), Collections.singletonList(requestId));
                }
                // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, Collections.singletonList(key), Collections.singletonList(requestId));
                }
                return 0L;
            }
        });
        return result != null && result > 0;
    }

    @SuppressWarnings("unchecked")
    public Object eval(final String script, final List<String> keys, final List<String> args) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof JedisCluster) { // 集群模式
                    return (Long) ((JedisCluster) nativeConnection).eval(script, keys, args);
                } else { // 单机模式
                    return (Long) ((Jedis) nativeConnection).eval(script, keys, args);
                }
            }
        });
    }

    /**
     * 根据key获取锁
     *
     * @param key     锁key
     * @param timeout 锁超时时间
     * @return true 或者 false
     */
    public Boolean lockWithKeyAndTimeout(String key, Long timeout) {
        if (null == key) {
            return false;
        }
        long duration = DEFAULT_CACHE_SEC;
        if (null != timeout && timeout > 0) {
            duration = timeout;
        }
        Expiration expiration = Expiration.seconds(duration);
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> connection.stringCommands().set(key.getBytes(), LOCK_STRING_VALUE.getBytes(), expiration, RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     * 根据key释放锁
     *
     * @param key 锁key
     */
    public void unlockWithKey(String key) {
        delete(key);
    }
}
