package com.cm.controller.student;

import com.cm.annotations.Log;
import com.cm.constant.SuccessConstant;
import com.cm.context.BaseContext;
import com.cm.dto.*;
import com.cm.enumeration.BusinessType;
import com.cm.result.Result;
import com.cm.service.StudentUserInfoService;
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

    @Autowired
    private StudentUserInfoService studentUserInfoService;

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
