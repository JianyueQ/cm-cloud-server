package com.cm.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 滑动窗口限流器属性
 *
 * @author 31373
 */
@Data
//@Component
@ConfigurationProperties(prefix = "cm.rate-limiter.redis.sliding-window")
public class RedisSlidingWindowRateLimiterProperties {

    /**
     * redis的key
     */
    private String key;
    /**
     * 时间窗口内的最大请求数
     */
    private int limit;
    /**
     * 窗口大小（秒）
     */
    private int windowSize;
}
