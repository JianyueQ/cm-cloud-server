package com.cm.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 31373
 */
@Component
@ConfigurationProperties(prefix = "cm.auth")
@Data
public class PathWhiteListProperties {
    private List<String> includePath;
    private List<String> excludePath;
}
