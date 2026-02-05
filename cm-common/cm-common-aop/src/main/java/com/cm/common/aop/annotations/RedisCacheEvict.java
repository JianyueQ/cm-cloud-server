package com.cm.common.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 31373
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheEvict {

    /**
     * 需要删除的缓存键配置
     */
    CacheKeyConfig[] value() default {};

    @interface CacheKeyConfig {
        /**
         * 缓存键前缀
         */
        String keyPrefix();
        /**
         * 缓存键配置项
         */
        String[] keyParts() default {};

        /**
         * 是否为模式键（用于删除所有以keyPrefix开头的键）
         * 如果为true，则会在keyPrefix后添加"*"通配符
         */
        boolean isPattern() default false;
    }
}
