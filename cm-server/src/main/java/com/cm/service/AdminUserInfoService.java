package com.cm.service;

import com.cm.dto.*;
import com.cm.result.PageResult;
import com.cm.vo.AdminUserInfoVO;
import com.cm.vo.AdminWithUserInfoVO;

import java.util.List;

/**
 * @author 31373
 */
public interface AdminUserInfoService {
    /**
     * 获取用户信息
     * @return 用户信息
     */
    AdminUserInfoVO getUserInfo();

    /**
     * 修改用户信息
     * @param adminUserInfoUpdateDTO 修改用户信息
     */
    void updateUserInfo(AdminUserInfoUpdateDTO adminUserInfoUpdateDTO);

    /**
     * 修改密码
     * @param resetPasswordDTO 修改密码
     */
    void updatePassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * 添加管理员用户
     * @param adminUserInfoDTO 添加用户
     */
    void addUser(AdminUserInfoDTO adminUserInfoDTO);

    /**
     * 修改管理员用户
     * @param adminUserInfoDTO 修改用户
     */
    void updateUser(AdminUserInfoDTO adminUserInfoDTO);

    /**
     * 删除管理员用户
     * @param ids 删除用户id
     */
    void deleteUser(List<Long> ids);

    /**
     * 获取管理员用户-分页查询
     * @param adminUserInfoPageDTO 管理员用户-分页查询
     * @return 管理员用户-分页查询
     */
    PageResult list(AdminUserInfoPageDTO adminUserInfoPageDTO);

    /**
     * 获取管理员用户-详情
     * @param id 管理员用户id
     * @return 管理员用户详情
     */
    AdminWithUserInfoVO get(Long id);

    /**
     * 重置密码
     *
     * @return 重置密码
     */
    String resetPwd(Long id);

    /**
     * 修改状态
     * @param adminUserInfoUpdateStatusDTO 修改状态
     */
    void updateStatus(AdminUserInfoUpdateStatusDTO adminUserInfoUpdateStatusDTO);

}
