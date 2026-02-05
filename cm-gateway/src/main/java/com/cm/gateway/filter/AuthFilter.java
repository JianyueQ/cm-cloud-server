package com.cm.gateway.filter;


import com.cm.common.core.constant.MapConstant;
import com.cm.common.core.constant.MessageConstant;
import com.cm.common.core.constant.RedisConstant;
import com.cm.common.core.exception.RequestQuestionException;
import com.cm.common.core.properties.JwtProperties;
import com.cm.gateway.properties.PathWhiteListProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证过滤器
 *
 * @author 31373
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    private final PathWhiteListProperties pathWhiteListProperties;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public AuthFilter(PathWhiteListProperties pathWhiteListProperties, JwtProperties jwtProperties, RedisTemplate<String, String> redisTemplate) {
        this.pathWhiteListProperties = pathWhiteListProperties;
        this.jwtProperties = jwtProperties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().toString();
        if (isExcludePath(path)) {
            return chain.filter(exchange);
        }
        // 获取配置的请求头名称
        String configuredTokenName = jwtProperties.getUserTokenName();

        // 首先尝试使用配置的名称获取token
        String token = request.getHeaders().getFirst(configuredTokenName);

        // 如果配置的名称获取不到，尝试常见的认证头名称变体
        if (token == null || token.isEmpty()) {
            // 尝试标准的Authorization头
            token = request.getHeaders().getFirst("Authorization");
        }
        if (token == null || token.isEmpty()) {
            token = request.getHeaders().getFirst("authorization");
        }
        if (token == null || token.isEmpty()) {
            // 尝试Authentication头的不同变体
            token = request.getHeaders().getFirst("Authentication");
        }
        if (token == null || token.isEmpty()) {
            token = request.getHeaders().getFirst("authentication");
        }
        if (token == null || token.isEmpty()) {
            // 返回401未授权
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            throw new RequestQuestionException(MessageConstant.PLEASE_LOGIN);
        }

        try {
            //处于登录状态 - 增强单点登录检查
            Map<Object, Object> tokenMap = redisTemplate.opsForHash().entries(RedisConstant.JWT_TOKEN_KEY + token);
            if (!tokenMap.isEmpty()) {
                // 获取用户ID
                String userId = tokenMap.get(MapConstant.ID).toString();
                // 检查用户当前会话是否有效（防止同一用户重复登录时当前token失效）
                Map<Object, Object> userMap = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + userId);
                if (token.equals(userMap.get(MapConstant.USER_TOKEN))) {
                    redisTemplate.expire(RedisConstant.JWT_TOKEN_KEY + token, RedisConstant.TOKEN_TTL, TimeUnit.HOURS);
                    redisTemplate.expire(RedisConstant.JWT_ID_KEY + userId, RedisConstant.TOKEN_TTL, TimeUnit.HOURS);
                    log.info("登录用户类型:{},登录的用户id为：{},请求的路径:{}", userMap.get(MapConstant.USER_TYPE), userId,path);
                    ServerWebExchange serverWebExchange = exchange.mutate().request(
                            builder -> builder.header("user-id", userId)).build();
                    return chain.filter(serverWebExchange);
                } else {
                    // 用户已在其他地方登录，当前会话已失效
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    throw new RequestQuestionException(MessageConstant.LOGIN_EXPIRED);
                }
            } else {
                //处于未登录状态
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                throw new RequestQuestionException(MessageConstant.PLEASE_LOGIN);
            }
        } catch (RequestQuestionException e) {
            throw e;
        } catch (Exception e) {
            log.error("解析令牌失败：{}", e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            throw new RequestQuestionException(MessageConstant.LOGIN_FAILED);
        }
    }

    private boolean isExcludePath(String string) {
        for (String path : pathWhiteListProperties.getExcludePath()) {
            if (pathMatcher.match(path, string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
