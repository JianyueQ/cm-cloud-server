package com.cm.user.controller.student;


import com.cm.common.aop.annotations.Log;
import com.cm.common.core.constant.SuccessConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.result.Result;
import com.cm.common.enumeration.BusinessType;
import com.cm.dto.ResetPasswordDTO;
import com.cm.dto.StudentUserInfoUpdateDTO;
import com.cm.user.service.StudentUserInfoService;
import com.cm.vo.StudentUserInfoVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentUserInfoController {

    private final StudentUserInfoService studentUserInfoService;

    public StudentUserInfoController(StudentUserInfoService studentUserInfoService) {
        this.studentUserInfoService = studentUserInfoService;
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userInfo")
    public Result<StudentUserInfoVO> getUserInfo() {
        return Result.success(studentUserInfoService.getUserInfo());
    }

    /**
     * 更新用户信息
     */
    @Log(title = "学生-用户信息-基本资料", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public Result<String> updateUserInfo(@Valid @RequestBody StudentUserInfoUpdateDTO studentUserInfoUpdateDTO) {
        log.info("更新用户信息:{}",studentUserInfoUpdateDTO);
        studentUserInfoService.updateUserInfo(studentUserInfoUpdateDTO);
        return Result.success(SuccessConstant.UPDATE_SUCCESS);
    }

    /**
     * 修改密码
     */
    @Log(title = "学生-用户信息-修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public Result<String> updatePassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("用户:{}:修改密码:{}", BaseContext.getCurrentId(), resetPasswordDTO);
        studentUserInfoService.updatePassword(resetPasswordDTO);
        return Result.success(SuccessConstant.UPDATE_SUCCESS);
    }


}
