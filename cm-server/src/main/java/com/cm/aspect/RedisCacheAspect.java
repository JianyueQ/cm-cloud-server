package com.cm.aspect;

import com.cm.annotations.RedisCache;
import com.cm.annotations.RedisCacheEvict;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author 31373
 */
@Slf4j
@Aspect
@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisCacheAspect {

    /**
     * Spring Expression Language (SpEL)表达式解析器，用于解析注解中的表达式
     */
    private final ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * Jackson库提供的JSON序列化/反序列化工具类
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 环绕通知处理缓存逻辑
     *
     * @param joinPoint  切点
     * @param redisCache 注解
     * @return 缓存结果
     * @throws Throwable 抛出异常
     */
    @Around("@annotation(redisCache)")
    public Object handleRedisCache(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        log.debug("进入RedisCache缓存处理...");
        // 构建缓存键
        String cacheKey = buildCacheKey(joinPoint, redisCache);
        // 从缓存中获取数据
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
        if (cachedValue != null) {
            log.info("从缓存中获取数据，缓存键: {}", cacheKey);
            //获取方法返回值类型
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取方法返回类型
            Class<?> returnType = signature.getReturnType();
            // 将JSON字符串转换为对象
            String jsonString = cachedValue.toString();
            // 使用ObjectMapper将JSON字符串转换为对象
            return objectMapper.readValue(jsonString, returnType);
        } else {
            log.info("缓存中没有数据，执行方法并缓存结果，缓存键: {}", cacheKey);
            // 执行原方法
            Object result = joinPoint.proceed();
            //转换成json字符串
            String json = objectMapper.writeValueAsString(result);

            redisTemplate.opsForValue().set(cacheKey, json, redisCache.expireTime(), redisCache.timeUnit());

            return result;
        }
    }

    @Around("@annotation(redisCacheEvict)")
    public Object redisCacheEvict(ProceedingJoinPoint joinPoint, RedisCacheEvict redisCacheEvict) throws Throwable {
        log.info("进入redisCacheEvict缓存处理...");
        //构建缓存的key
        List<String> cacheKeys = buildCacheKeys(joinPoint, redisCacheEvict);
        //构建缓存的key
        List<String> patternKeys = buildPatternKeys(joinPoint, redisCacheEvict);

        // 删除指定的缓存键
        if (!cacheKeys.isEmpty()) {
            redisTemplate.delete(cacheKeys);
            log.info("删除缓存成功，缓存键: {}", cacheKeys);
        }

        // 删除匹配模式的缓存键
        for (String pattern : patternKeys) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("通过模式删除缓存成功，模式: {}，匹配到的键数量: {}", pattern, keys.size());
            }
        }

        return joinPoint.proceed();
    }

    /**
     * 构建模式键列表
     */
    protected List<String> buildPatternKeys(ProceedingJoinPoint joinPoint, RedisCacheEvict redisCacheEvict) {
        List<String> patternKeys = new ArrayList<>();

        // 处理每个CacheKeyConfig配置
        for (RedisCacheEvict.CacheKeyConfig keyConfig : redisCacheEvict.value()) {
            // 只处理模式键（含通配符）
            if (keyConfig.isPattern()) {
                String pattern = buildPatternKeyForConfig(joinPoint, keyConfig);
                if (pattern != null && !pattern.isEmpty()) {
                    patternKeys.add(pattern);
                }
            }
        }
        return patternKeys;
    }

    /**
     * 构建多个缓存键
     */
    protected List<String> buildCacheKeys(ProceedingJoinPoint joinPoint, RedisCacheEvict redisCacheEvict) {
        List<String> cacheKeys = new ArrayList<>();

        // 处理每个CacheKeyConfig配置
        for (RedisCacheEvict.CacheKeyConfig keyConfig : redisCacheEvict.value()) {
            // 只处理非模式键（不含通配符）
            if (!keyConfig.isPattern()) {
                List<String> keys = buildCacheKeyForConfig(joinPoint, keyConfig);
                cacheKeys.addAll(keys);
            }
        }
        return cacheKeys;
    }

    /**
     * 根据单个CacheKeyConfig构建缓存键列表
     */
    protected List<String> buildCacheKeyForConfig(ProceedingJoinPoint joinPoint, RedisCacheEvict.CacheKeyConfig keyConfig) {
        List<String> cacheKeys = new ArrayList<>();
        StringBuilder keyBuilder = new StringBuilder();

        if (!keyConfig.keyPrefix().isEmpty()) {
            keyBuilder.append(keyConfig.keyPrefix());
        } else {
            //默认使用类名+方法名作为前缀
            keyBuilder.append(joinPoint.getTarget().getClass().getSimpleName())
                    .append(":")
                    .append(joinPoint.getSignature().getName())
                    .append(":");
        }

        //添加键的组成部分
        if (keyConfig.keyParts().length > 0) {
            //获取参数
            Object[] args = joinPoint.getArgs();
            //获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取参数名称
            String[] paramNames = signature.getParameterNames();
            //创建一个EvaluationContext对象,根据方法参数动态生成Redis缓存键
            EvaluationContext context = new StandardEvaluationContext();
            //循环参数名称和参数值,设置到EvaluationContext对象中
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }

            //获取缓存的key
            for (String keyPart : keyConfig.keyParts()) {
                Expression expression = parser.parseExpression(keyPart);
                //获取key
                Object value = expression.getValue(context);

                // 如果值是集合类型，则为每个元素生成一个键
                if (value instanceof Collection) {
                    Collection<?> collection = (Collection<?>) value;
                    for (Object item : collection) {
                        // 为集合中的每个元素创建一个完整的键
                        StringBuilder itemBuilder = new StringBuilder(keyBuilder);
//                        itemBuilder.append(item != null ? item.toString() : "null").append("_");
                        if (item != null) {
                            itemBuilder.append(item.toString()).append("_");
                        } else {
                            itemBuilder.append(":null");
                        }
                        cacheKeys.add(itemBuilder.toString());
                    }
                } else {
                    // 拼接key
                    keyBuilder.append(value != null ? value.toString() : "null").append("_");
                }
            }

            // 如果没有生成基于集合的键，则添加单个键
            if (cacheKeys.isEmpty()) {
//                cacheKeys.add(keyBuilder.toString());
                //去除结尾的下划线
                if (!keyBuilder.isEmpty() && keyBuilder.charAt(keyBuilder.length() - 1) == '_') {
                    keyBuilder.deleteCharAt(keyBuilder.length() - 1);
                }
                cacheKeys.add(keyBuilder.toString());
            }
        } else {
            // 没有keyParts的情况
            cacheKeys.add(keyBuilder.toString());
        }

        return cacheKeys;
    }

    /**
     * 构建模式键
     */
    protected String buildPatternKeyForConfig(ProceedingJoinPoint joinPoint, RedisCacheEvict.CacheKeyConfig keyConfig) {
        StringBuilder keyBuilder = new StringBuilder();

        if (!keyConfig.keyPrefix().isEmpty()) {
            keyBuilder.append(keyConfig.keyPrefix());
        } else {
            //默认使用类名+方法名作为前缀
            keyBuilder.append(joinPoint.getTarget().getClass().getSimpleName())
                    .append(":")
                    .append(joinPoint.getSignature().getName());
        }

        // 如果是模式键，添加通配符
        if (keyConfig.isPattern()) {
            keyBuilder.append("*");
        }

        return keyBuilder.toString();
    }


    /**
     * 构建缓存键
     */
    protected String buildCacheKey(ProceedingJoinPoint joinPoint, RedisCache redisCache) {
        //创建一个可变的字符串序列,StringBuilder是可变的，可以在原有对象上直接修改,减少性能的消耗
        StringBuilder keyBuilder = new StringBuilder();

        if (!redisCache.keyPrefix().isEmpty()) {
            keyBuilder.append(redisCache.keyPrefix());
        } else {
            //默认使用类名+方法名作为前缀
            keyBuilder.append(joinPoint.getTarget().getClass().getSimpleName())
                    .append(":")
                    .append(joinPoint.getSignature().getName())
                    .append(":");
        }

        //添加键的组成部分
        if (redisCache.keyParts().length > 0) {
            //获取参数
            Object[] args = joinPoint.getArgs();
            //获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取参数名称
            String[] paramNames = signature.getParameterNames();
            //创建一个EvaluationContext对象,根据方法参数动态生成Redis缓存键
            EvaluationContext context = new StandardEvaluationContext();
            //循环参数名称和参数值,设置到EvaluationContext对象中
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            //获取缓存的key
            for (String keyPart : redisCache.keyParts()) {

                Expression expression = parser.parseExpression(keyPart);
                //获取key
                Object value = expression.getValue(context);
                //拼接key
                keyBuilder.append(value != null ? value.toString() : "null").append("_");
            }
        }
        //去除结尾的下划线
        if (!keyBuilder.isEmpty() && keyBuilder.charAt(keyBuilder.length() - 1) == '_') {
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        return keyBuilder.toString();
    }

}
