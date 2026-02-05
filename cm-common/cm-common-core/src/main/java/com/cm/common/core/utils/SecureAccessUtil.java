package com.cm.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 31373
 */
@Slf4j
public class SecureAccessUtil {

    /**
     * 安全获取 Integer 值
     */
    public static Integer getIntegerValue(Map<String, Object> map, String key, Integer defaultValue) {
        Object value = map.get(key);
        if (value == null) {
            log.warn("消息中缺少字段: {}，使用默认值: {}", key, defaultValue);
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            log.warn("{} 字段格式错误: {}，使用默认值: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 安全获取 Long 值
     */
    public static Long getLongValue(Map<String, Object> map, String key, Long defaultValue) {
        Object value = map.get(key);
        if (value == null) {
            log.warn("消息中缺少字段: {}，使用默认值: {}", key, defaultValue);
            return defaultValue;
        }
        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException e) {
            log.warn("{} 字段格式错误: {}，使用默认值: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 安全获取 String 值
     */
    public static String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null) {
            log.warn("消息中缺少字段: {}，使用默认值: {}", key, defaultValue);
            return defaultValue;
        }
        return value.toString();
    }

    /**
     * 安全获取 LocalDateTime 值
     */
    public static LocalDateTime getLocalDateTimeValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            log.warn("消息中缺少时间字段: {}", key);
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(value.toString());
        } catch (Exception e) {
//            log.warn("{} 时间字段格式错误: {}", key, value);
            return LocalDateTime.now();
        }
    }

}
