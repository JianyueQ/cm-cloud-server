package com.cm.common.alibabaOSS.config;

import com.cm.common.alibabaOSS.properties.AliOssProperties;
import com.cm.common.alibabaOSS.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 31373
 */
@Configuration
@Slf4j
public class AliossConfig {

    /**
     * 声明阿里云oss的bean
     */
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云oss的bean...");
        return new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }


}
