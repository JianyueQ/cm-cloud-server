package com.cm.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 31373
 */
@Data
//@Component
@ConfigurationProperties(prefix = "cm.rate-limiter.redis.token-bucket")
public class RedisTokenBucketRateLimiterProperties {

    /**
     * redis的key
     */
    private String key;
    /**
     * 每秒的令牌数
     */
    private int permitsPerSecond;
    /**
     * 桶的容量
     */
    private int bucketCapacity;
}
