package com.cm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Redis原子操作工具类
 *
 * @author 31373
 */
@Slf4j
@Component
public class RedisAtomicUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 原子性地减少Hash中的数值（格式：currentCount,maxCount）
     * 如果currentCount > 0，则减1并返回true，否则返回false
     * 如果缓存不存在，会从数据库加载数据到缓存后再执行操作
     *
     * @param key     Hash键
     * @param field   Hash字段
     * @return        操作是否成功
     */
    public boolean decrementHashField(String key, String field) {
        String luaScript =
                "local current = redis.call('HGET',KEYS[1],ARGV[1])" +
                "if current then" +
                "  local parts = {}" +
                "  for part in string.gmatch(current,'[^,]+') do" +
                "    table.insert(parts,part)" +
                "  end" +
                "  local currentCount = tonumber(parts[1])" +
                "  local maxCount = tonumber(parts[2])" +
                "  if currentCount > 0 then " +
                "    currentCount = currentCount - 1 " +
                "    local updated = currentCount .. ',' .. maxCount " +
                "    redis.call('HSET', KEYS[1], ARGV[1], updated) " +
                "    redis.call('EXPIRE', KEYS[1], 3600) " +
                "    return 1 " +
                "  end " +
                "end " +
                "return 0";
        //创建脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //设置脚本文本
        redisScript.setScriptText(luaScript);
        //设置脚本返回值类型为Long
        redisScript.setResultType(Long.class);
        List<String> keys = Collections.singletonList(key);
        List<String> args = Collections.singletonList(field);
        Long result = redisTemplate.execute(redisScript, keys, args.toArray(new String[0]));
        return result != null && result == 1;
    }

    /**
     * 检查指定的Hash字段是否存在
     *
     * @param key   Hash键
     * @param field Hash字段
     * @return      是否存在
     */
    public boolean hasHashField(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 使用Lua脚本和SCAN方式删除匹配模式的缓存键（适用于大数据量场景）
     * 避免KEYS命令可能引起的Redis阻塞问题
     *
     * @param pattern 键模式，例如："course:page:*" 或 "course:page:admin:*"
     * @return 删除的键数量
     */
    public Long deleteKeysByPatternWithLuaScan(String pattern) {
        try {
            // Lua脚本：使用SCAN迭代方式查找并删除所有匹配模式的键
            String luaScript =
                    "local cursor = '0' " +
                            "local total_deleted = 0 " +
                            "repeat " +
                            "  local result = redis.call('SCAN', cursor, 'MATCH', ARGV[1], 'COUNT', 100) " +
                            "  cursor = result[1] " +
                            "  local keys = result[2] " +
                            "  if #keys > 0 then " +
                            "    local deleted = redis.call('DEL', unpack(keys)) " +
                            "    total_deleted = total_deleted + deleted " +
                            "  end " +
                            "until cursor == '0' " +
                            "return total_deleted";

            // 创建Redis脚本对象
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(luaScript);
            script.setResultType(Long.class);

            // 执行脚本，传入模式参数
            return redisTemplate.execute(
                    script,
                    // KEYS参数列表
                    Collections.emptyList(),
                    // ARGV参数列表（模式）
                    pattern
            );
        } catch (Exception e) {
            throw new RuntimeException("使用Lua脚本和SCAN方式删除匹配模式的缓存键时发生异常: " + pattern, e);
        }
    }
}
