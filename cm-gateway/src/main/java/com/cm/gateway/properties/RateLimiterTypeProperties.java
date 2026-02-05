package com.cm.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 31373
 */
@Data
@Component
@ConfigurationProperties(prefix = "cm.rate-limiter")
public class RateLimiterTypeProperties {

    private String type;

}
