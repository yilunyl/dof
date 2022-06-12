package com.yilun.gl.dof.excute.framework.lock;

/**
 * @author gl
 * <p>此分布式锁主要用于多节点间的锁问题，使用redis共享锁，建议直接使用RedisUtil</p>
 * @date 2018/5/10
 * @Description: 分布式锁
 * <p>如果要使用此API，尽量在lock前后使用双端验证</p>
 * <p>一定要在finally从句中使用unlock解除锁</p>
 */
public class DistributedLockUtil {

    private static final int DEFAULT_VALUE = 1;
    private static final int TIMES = 10;
    private static final long THREAD_SLEEP_TIME = 200L;

    public static void lock(DofRedisUtil redisUtil, String key) throws InterruptedException {

        int times = 0;
        while (!redisUtil.setIfAbsent(key, DEFAULT_VALUE) && times < TIMES) {
            times++;
            Thread.sleep(THREAD_SLEEP_TIME);
        }
        if (times == TIMES) {
            unlock(redisUtil, key);
        }
    }

    /**
     * 根据key释放锁
     *
     * @param redisUtil redis
     * @param key       锁key
     * @deprecated 直接用RedisUtil
     */
    public static void unlock(DofRedisUtil redisUtil, String key) {
        redisUtil.unlockWithKey(key);
    }

    /**
     * @param redisUtil redis
     * @param key       key
     * @param timeout   过期时间(如果不设置，默认1天)
     * @return 获取成功返回true， 否则返回false
     * @deprecated 直接用RedisUtil
     */
    public static boolean lock(DofRedisUtil redisUtil, String key, Long timeout) {
        Boolean flag = redisUtil.lockWithKeyAndTimeout(key, timeout);
        if (null == flag) {
            return false;
        }
        return flag;
    }
}
