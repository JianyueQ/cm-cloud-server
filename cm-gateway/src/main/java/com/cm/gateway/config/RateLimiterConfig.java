package com.cm.gateway.config;

import com.cm.gateway.properties.RedisSlidingWindowRateLimiterProperties;
import com.cm.gateway.properties.RedisTokenBucketRateLimiterProperties;
import com.cm.gateway.rateLimiter.RedisSlidingWindowRateLimiter;
import com.cm.gateway.rateLimiter.RedisTokenBucketRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 31373
 */
@Configuration
@Slf4j
public class RateLimiterConfig {

    /**
     * redis滑动窗口限流器
     *
     * @param redisTemplate redisTemplate
     * @return 滑动窗口限流器
     */
    @Bean
    @ConditionalOnProperty(name = "cm.rate-limiter.type", havingValue = "RedisSlidingWindowRateLimiter", matchIfMissing = false)
    public RedisSlidingWindowRateLimiter slidingWindowRateLimiter(RedisTemplate<String, String> redisTemplate) {
        log.info("开始初始化滑动窗口限流器......");
        return new RedisSlidingWindowRateLimiter(redisTemplate);
    }

    /**
     * 滑动窗口限流器配置
     *
     * @return 滑动窗口限流器配置
     */
    @Bean
    @ConditionalOnProperty(name = "cm.rate-limiter.type", havingValue = "RedisSlidingWindowRateLimiter", matchIfMissing = false)
    public RedisSlidingWindowRateLimiterProperties redisSlidingWindowRateLimiterProperties() {
        log.info("开始初始化滑动窗口限流器配置......");
        return new RedisSlidingWindowRateLimiterProperties();
    }

    /**
     * redis令牌桶限流器
     *
     * @param redisTemplate redisTemplate
     * @return 令牌桶限流器
     */
    @Bean
    @ConditionalOnProperty(name = "cm.rate-limiter.type", havingValue = "RedisTokenBucketRateLimiter", matchIfMissing = false)
    public RedisTokenBucketRateLimiter redisTokenBucketRateLimiter(RedisTemplate<String, String> redisTemplate) {
        log.info("开始初始化令牌桶限流器......");
        return new RedisTokenBucketRateLimiter(redisTemplate);
    }

    /**
     * 令牌桶限流器配置
     *
     * @return 令牌桶限流器配置
     */
    @Bean
    @ConditionalOnProperty(name = "cm.rate-limiter.type", havingValue = "RedisTokenBucketRateLimiter", matchIfMissing = false)
    public RedisTokenBucketRateLimiterProperties tokenBucketRateLimiterProperties() {
        log.info("开始初始化令牌桶限流器配置......");
        return new RedisTokenBucketRateLimiterProperties();
    }
}
