package com.cm.auth.controller.login;

import com.cm.auth.service.StudentLoginService;
import com.cm.common.core.constant.SuccessConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.result.Result;
import com.cm.dto.StudentLoginDTO;
import com.cm.vo.StudentLoginVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生认证-控制器
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentLoginController {

    private final StudentLoginService studentLoginService;

    public StudentLoginController(StudentLoginService studentLoginService) {
        this.studentLoginService = studentLoginService;
    }

    /**
     * 学生登录
     */
    @PostMapping("/login")
    public Result<StudentLoginVO> login(@Valid @RequestBody StudentLoginDTO studentLoginDTO) {
        log.info("学生登录数据:{}", studentLoginDTO);
        return Result.success(studentLoginService.login(studentLoginDTO));
    }

    /**
     * 学生登出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        Long currentId = BaseContext.getCurrentId();
        log.info("学生:{}登出", currentId);
        studentLoginService.logout(currentId);
        return Result.success(SuccessConstant.LOGOUT_SUCCESS);
    }
}
