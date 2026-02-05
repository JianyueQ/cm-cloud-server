package com.cm.user.controller.common;

import com.cm.entity.User;
import com.cm.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息-控制器
 *
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/all/userInfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/getUserInfo/{id}")
    public User getUserInfo(@PathVariable Long id) {
        log.info("根据id查询用户信息: {}", id);
        return userInfoService.getUserInfoById(id);
    }

    


}
