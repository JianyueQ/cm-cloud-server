package com.cm.gateway.rateLimiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

/**
 * 滑动窗口限流器
 *
 * @author 31373
 */
@Slf4j
public class RedisSlidingWindowRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisSlidingWindowRateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 滑动窗口限流
     *
     * @param key        限流的标识（如用户ID、IP地址等）
     * @param limit      时间窗口内的最大请求数
     * @param windowSize 窗口大小（秒）
     * @return 是否允许请求通过
     */
    public boolean isAllowed(String key, int limit, int windowSize) {
        String luaScript =
                "local key = KEYS[1] " +
                        // 最大请求数
                        "local limit = tonumber(ARGV[1]) " +
                        // 窗口大小（秒）
                        "local window = tonumber(ARGV[2]) " +
                        // 当前时间戳
                        "local current_time = redis.call('TIME') " +
                        // 当前时间
                        "local now = tonumber(current_time[1]) " +
                        // 请求数
                        "local requests = redis.call('ZRANGEBYSCORE', key, now - window, now) " +
                        // 判断是否超出限制
                        "if #requests >= limit then " +
                        "  return 0 " +
                        "else " +
                        "  redis.call('ZADD', key, now, now .. '-' .. math.random(1000000)) " +
                        "  redis.call('EXPIRE', key, window + 10) " +
                        "  return 1 " +
                        "end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisTemplate.execute(
                redisScript, List.of(key),
                String.valueOf(limit),
                String.valueOf(windowSize));
        return result == 1;
    }

    /**
     * 获取当前窗口内的请求数
     *
     * @param key        限流的标识
     * @param windowSize 窗口大小（秒）
     * @return 当前窗口内的请求数
     */
    public long getCurrentCount(String key, int windowSize) {
        String luaScript =
                "local key = KEYS[1] " +
                        "local window = tonumber(ARGV[1]) " +
                        "local current_time = redis.call('TIME') " +
                        "local now = tonumber(current_time[1]) " +
                        "redis.call('ZREMRANGEBYSCORE', key, 0, now - window) " +
                        "return redis.call('ZCARD', key)";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        return redisTemplate.execute(
                redisScript, List.of(key),
                String.valueOf(windowSize));
    }


    /**
     * 重置限流计数器
     *
     * @param key 限流的标识
     */
    public void reset(String key) {
        redisTemplate.delete(key);
    }
}
