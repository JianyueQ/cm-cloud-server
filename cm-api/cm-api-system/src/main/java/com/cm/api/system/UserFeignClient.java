package com.cm.api.system;



import com.cm.api.system.fallback.UserFeignClientFallback;
import com.cm.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务Feign客户端
 *
 * @author 31373
 */
@FeignClient(value = "cm-user", fallback = UserFeignClientFallback.class)
public interface UserFeignClient {

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/all/userInfo/getUserInfo/{id}")
    User getUserInfo(@PathVariable Long id);

}
