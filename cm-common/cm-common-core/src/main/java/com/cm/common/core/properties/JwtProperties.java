package com.cm.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt令牌相关属性
 * @author 31373
 */
@Component
@ConfigurationProperties(prefix = "cm.jwt")
@Data
public class JwtProperties {
    /**
     * 用户端用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;



}
