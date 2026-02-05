package com.cm.gateway.filter;

import com.cm.common.core.constant.MessageConstant;
import com.cm.common.core.exception.RequestQuestionException;
import com.cm.common.core.utils.SpringUtils;
import com.cm.gateway.properties.RateLimiterTypeProperties;
import com.cm.gateway.properties.RedisSlidingWindowRateLimiterProperties;
import com.cm.gateway.properties.RedisTokenBucketRateLimiterProperties;
import com.cm.gateway.rateLimiter.RedisSlidingWindowRateLimiter;
import com.cm.gateway.rateLimiter.RedisTokenBucketRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author 31373
 */
@Slf4j
@Component
public class RateLimiterFilter implements GlobalFilter, Ordered {

    private final RateLimiterTypeProperties rateLimiterTypeProperties;

    public RateLimiterFilter(RateLimiterTypeProperties rateLimiterTypeProperties) {
        this.rateLimiterTypeProperties = rateLimiterTypeProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String type = rateLimiterTypeProperties.getType();
        log.info("限流类型：{}", type);
        // 从请求头中获取userId，如果不存在则使用IP地址作为key
        String user = exchange.getRequest().getHeaders().getFirst("user-id");
        if (user == null){
            user = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
        }

        //限流方法
        Boolean isAllowed;
        switch (type) {
            case "RedisSlidingWindowRateLimiter":
                isAllowed = redisSlidingWindowRateLimiter(user);
                if (isAllowed){
                    log.info("用户:{},限流结果:{}",user, true);
                    return chain.filter(exchange);
                }else {
                    throw new RequestQuestionException(MessageConstant.TOO_MANY_REQUESTS);
                }

            case "RedisTokenBucketRateLimiter":
                isAllowed = redisTokenBucketRateLimiter(user);
                if (isAllowed){
                    log.info("用户:{},限流结果:{}",user, true);
                    return chain.filter(exchange);
                }else {
                    throw new RequestQuestionException(MessageConstant.TOO_MANY_REQUESTS);
                }
            default:
                throw new RequestQuestionException(MessageConstant.TOO_MANY_REQUESTS);
        }
    }

    private Boolean redisTokenBucketRateLimiter(String user) {
        // 使用SpringUtils获取Bean实例
        RedisTokenBucketRateLimiter rateLimiter = SpringUtils.getBean(RedisTokenBucketRateLimiter.class);
        RedisTokenBucketRateLimiterProperties properties = SpringUtils.getBean(RedisTokenBucketRateLimiterProperties.class);

        // 使用配置中的参数
        String key = properties.getKey() + ":" + user;
        int permitsPerSecond = properties.getPermitsPerSecond();
        int bucketCapacity = properties.getBucketCapacity();

        return rateLimiter.tryAcquire(key, permitsPerSecond, bucketCapacity);
    }

    private Boolean redisSlidingWindowRateLimiter(String user) {
        // 使用SpringUtils获取Bean实例
        RedisSlidingWindowRateLimiter rateLimiter = SpringUtils.getBean(RedisSlidingWindowRateLimiter.class);
        RedisSlidingWindowRateLimiterProperties properties = SpringUtils.getBean(RedisSlidingWindowRateLimiterProperties.class);

        // 使用配置中的参数
        String key = properties.getKey() + ":" + user;
        int limit = properties.getLimit();
        int windowSize = properties.getWindowSize();

        return rateLimiter.isAllowed(key, limit, windowSize);
    }


    @Override
    public int getOrder() {
        return -500;
    }
}
