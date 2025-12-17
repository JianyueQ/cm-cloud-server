package com.cm.interceptor;

import com.cm.constant.MapConstant;
import com.cm.constant.MessageConstant;
import com.cm.constant.RedisConstant;
import com.cm.context.BaseContext;
import com.cm.exception.RequestQuestionException;
import com.cm.properties.JwtProperties;
import com.cm.rateLimiter.RedisTokenBucketRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Component
@Slf4j
public class TeacherInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisTokenBucketRateLimiter redisTokenBucketRateLimiter;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否为controller的请求,如果不是就放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //从请求头中获取令牌
        String authorization = request.getHeader(jwtProperties.getTeacherTokenName());
        if (authorization == null || authorization.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new RequestQuestionException(MessageConstant.PLEASE_LOGIN);
        }
        try {
            //处于登录状态 - 增强单点登录检查
            Map<Object, Object> tokenMap = redisTemplate.opsForHash().entries(RedisConstant.JWT_TOKEN_KEY + authorization);
            if (!tokenMap.isEmpty()) {
                // 获取用户ID
                String userId = tokenMap.get(MapConstant.ID).toString();
                // 检查用户当前会话是否有效（防止同一用户重复登录时当前token失效）
                Map<Object, Object> userMap = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + userId);
                if (authorization.equals(userMap.get(MapConstant.USER_TOKEN))) {
                    redisTemplate.expire(RedisConstant.JWT_TOKEN_KEY + authorization, RedisConstant.TOKEN_TTL, TimeUnit.HOURS);
                    redisTemplate.expire(RedisConstant.JWT_ID_KEY + userId, RedisConstant.TOKEN_TTL, TimeUnit.HOURS);
                    BaseContext.setCurrentId(Long.parseLong(userId));
                    log.info("登录用户类型:{},当前登录的用户id为：{}", userMap.get(MapConstant.USER_TYPE), BaseContext.getCurrentId());

                    //基于用户id进行限流
                    //每个用户每秒最多1个请求,最大突发2个请求
                    boolean tryAcquire = redisTokenBucketRateLimiter.tryAcquire(userId, 1, 4);
                    log.info("用户id:{},限流结果:{}", userId, tryAcquire);
                    if (!tryAcquire) {
                        response.setStatus(429);
                        throw new RequestQuestionException(MessageConstant.TOO_MANY_REQUESTS);
                    }
                    return true;
                } else {
                    // 用户已在其他地方登录，当前会话已失效
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    throw new RequestQuestionException(MessageConstant.LOGIN_EXPIRED);
                }
            }
            //处于未登录状态
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new RequestQuestionException(MessageConstant.PLEASE_LOGIN);
        } catch (RequestQuestionException e) {
            log.error("{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("解析令牌失败：{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new RequestQuestionException(MessageConstant.LOGIN_FAILED);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //销毁线程
        log.info("线程已销毁：{}", BaseContext.getCurrentId());
        BaseContext.removeCurrentId();
    }


}
