package com.cm.controller.student;

import com.cm.constant.SuccessConstant;
import com.cm.context.BaseContext;
import com.cm.dto.StudentLoginDTO;
import com.cm.result.Result;
import com.cm.service.StudentLoginService;
import com.cm.vo.StudentLoginVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentLoginController {

    @Autowired
    private StudentLoginService studentLoginService;

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
