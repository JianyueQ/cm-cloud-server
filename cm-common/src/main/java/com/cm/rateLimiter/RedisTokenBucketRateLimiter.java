package com.cm.rateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author 31373
 */
@Component
public class RedisTokenBucketRateLimiter {

    private static final String KEY_PREFIX = "rate_limiter:";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean tryAcquire(String key, int permitsPerSecond, int bucketCapacity) {
        String script =
                "local key = KEYS[1] " +
                        // 每秒令牌数
                        "local rate = tonumber(ARGV[1]) " +
                        // 桶容量
                        "local capacity = tonumber(ARGV[2]) " +
                        // 当前时间戳
                        "local now = tonumber(ARGV[3]) " +
                        // 请求令牌数
                        "local requested = tonumber(ARGV[4]) " +

                        // 获取当前桶中的令牌数和最后更新时间
                        "local bucket = redis.call('HMGET', key, 'tokens', 'last_update') " +
                        "local tokens = tonumber(bucket[1]) or capacity " +
                        "local last_update = tonumber(bucket[2]) or now " +

                        // 计算时间间隔
                        "local delta = (now - last_update) / 1000 " +
                        "local new_tokens = math.min(capacity, tokens + delta * rate) " +

                        // 判断是否有足够令牌
                        "if new_tokens >= requested then " +
                        "  redis.call('HMSET', key, 'tokens', new_tokens - requested, 'last_update', now) " +
                        // 设置过期时间
                        "  redis.call('EXPIRE', key, 3600) " +
                        "  return 1 " +
                        "else " +
                        "  redis.call('HMSET', key, 'tokens', new_tokens, 'last_update', now) " +
                        "  redis.call('EXPIRE', key, 3600) " +
                        "  return 0 " +
                        "end";
        List<String> keys = List.of(KEY_PREFIX + key);
        List<String> args = Arrays.asList(
                // 每秒令牌数
                String.valueOf(permitsPerSecond),
                // 桶容量
                String.valueOf(bucketCapacity),
                String.valueOf(System.currentTimeMillis()),
                // 请求1个令牌
                "1"
        );
        // 执行Lua脚本
        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                keys,
                args.toArray()
        );

        return result == 1;
    }

}
