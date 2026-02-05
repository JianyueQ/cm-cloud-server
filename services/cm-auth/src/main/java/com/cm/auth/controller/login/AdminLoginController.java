package com.cm.auth.controller.login;

import com.cm.auth.service.AdminLoginService;

import com.cm.common.core.constant.SuccessConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.result.Result;
import com.cm.dto.AdminLoginDTO;

import com.cm.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员认证-控制器
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    private final AdminLoginService adminLoginService;

    public AdminLoginController(AdminLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理员登录数据:{}", adminLoginDTO);
        return Result.success(adminLoginService.login(adminLoginDTO));
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        Long currentId = BaseContext.getCurrentId();
        log.info("管理员:{}登出", currentId);
        adminLoginService.logout(currentId);
        return Result.success(SuccessConstant.LOGOUT_SUCCESS);
    }
}
