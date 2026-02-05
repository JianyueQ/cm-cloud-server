package com.cm.common.alibabaOSS.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云文件上传配置
 * @author 31373
 */
@Component
@ConfigurationProperties(prefix = "cm.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
