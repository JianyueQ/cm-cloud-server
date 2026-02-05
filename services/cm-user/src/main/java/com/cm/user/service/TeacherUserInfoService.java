package com.cm.user.service;

import com.cm.dto.ResetPasswordDTO;
import com.cm.dto.TeacherUserInfoUpdateDTO;
import com.cm.vo.TeacherUserInfoVO;
import jakarta.validation.Valid;

/**
 * @author 31373
 */
public interface TeacherUserInfoService {


    /**
     * 修改密码
     * @param resetPasswordDTO  修改密码
     */
    void updatePassword(@Valid ResetPasswordDTO resetPasswordDTO);

    /**
     * 获取用户信息
     * @return 用户信息
     */
    TeacherUserInfoVO getUserInfo();

    /**
     * 修改用户信息
     * @param teacherUserInfoUpdateDTO 修改用户信息
     */
    void updateUserInfo(@Valid TeacherUserInfoUpdateDTO teacherUserInfoUpdateDTO);

}
