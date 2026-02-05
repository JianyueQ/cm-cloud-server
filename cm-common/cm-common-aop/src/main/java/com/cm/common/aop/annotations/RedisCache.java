package com.cm.common.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {

    /**
     * 缓存的前缀
     */
    String keyPrefix() default "";

    /**
     * 缓存过期时间，默认1小时
     */
    long expireTime() default 1;

    /**
     * 时间单位，默认为小时
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.HOURS;

    /**
     * 键的组成部分，使用SpEL表达式
     * 例如: "#id" 或 "#param.name" 等
     */
    String[] keyParts() default {};


}
