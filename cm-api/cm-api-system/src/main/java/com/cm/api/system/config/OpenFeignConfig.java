package com.cm.api.system.config;

import feign.Logger;
import feign.Retryer;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign配置类
 * @author 31373
 */

@Configuration
public class OpenFeignConfig {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(OpenFeignConfig.class);

    @Bean
    Logger.Level feignLoggerLevel() {
        log.info("初始化OpenFeign日志.................");
        return Logger.Level.FULL;
    }

    /**
     * 重试机制
     */
//    @Bean
    public Retryer feignRetryer() {
        // 100毫秒重试一次，重试3次
        return new Retryer.Default(100, 1000, 3);
    }

}
