package com.cm.api.system.fallback;


import com.cm.api.system.UserFeignClient;
import com.cm.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务Feign客户端备用方案
 *
 * @author 31373
 */
@Component
public class UserFeignClientFallback implements UserFeignClient {

    private static final Logger log = LoggerFactory.getLogger(UserFeignClientFallback.class);

    @Override
    public User getUserInfo(Long id) {
        log.info("用户服务Feign客户端备用方案");
        User user = new User();
        user.setRealName("未知用户");
        user.setUserType(0);
        return user;
    }
}
