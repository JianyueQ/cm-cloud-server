package com.cm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 31373
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * 列名
     */
    String name() default "";

    /**
     * 列的顺序
     */
    int sort() default 0;

    /**
     * 字段默认值
     */
    String defaultValue() default "";

}
