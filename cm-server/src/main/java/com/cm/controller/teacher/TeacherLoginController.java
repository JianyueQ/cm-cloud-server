package com.cm.controller.teacher;

import com.cm.constant.SuccessConstant;
import com.cm.context.BaseContext;
import com.cm.dto.TeacherLoginDTO;
import com.cm.result.Result;
import com.cm.service.TeacherLoginService;
import com.cm.vo.TeacherLoginVO;
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
@RequestMapping("/teacher")
public class TeacherLoginController {

    @Autowired
    private TeacherLoginService teacherLoginService;

    /**
     * 教师登录
     */
    @PostMapping("/login")
    public Result<TeacherLoginVO> login(@Valid @RequestBody TeacherLoginDTO teacherLoginDTO) {
        log.info("教师登录数据:{}", teacherLoginDTO);
        return Result.success(teacherLoginService.login(teacherLoginDTO));
    }

    /**
     * 教师登出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        Long currentId = BaseContext.getCurrentId();
        log.info("教师:{}登出", currentId);
        teacherLoginService.logout(currentId);
        return Result.success(SuccessConstant.LOGOUT_SUCCESS);
    }
}
