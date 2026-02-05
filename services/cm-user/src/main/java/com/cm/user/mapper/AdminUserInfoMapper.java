package com.cm.user.mapper;

import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.enumeration.OperationType;
import com.cm.dto.AdminUserInfoPageDTO;
import com.cm.dto.AdminUserInfoUpdateStatusDTO;
import com.cm.entity.Admin;
import com.cm.entity.User;

import com.cm.vo.AdminUserInfoVO;
import com.cm.vo.AdminWithUserInfoPageVO;
import com.cm.vo.AdminWithUserInfoVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface AdminUserInfoMapper {
    /**
     * 获取用户信息
     * @param currentId 当前用户id
     * @return 用户信息
     */
    AdminUserInfoVO getUserInfoById(Long currentId);

    /**
     * 修改用户信息
     * @param user 修改用户信息
     */
    void updateUserInfo(User user);

    /**
     * 根据id查询用户
     * @param currentId 当前用户id
     * @return  用户信息
     */
    User selectById(Long currentId);

    /**
     * 添加管理员用户
     * @param user 添加用户
     */
    @AutoFile(OperationType.INSERT)
    void insertUser(User user);

    /**
     * 添加管理员用户
     * @param admin 添加用户
     */
    @AutoFile(OperationType.INSERT)
    void insertAdmin(Admin admin);

    /**
     * 修改管理员用户
     * @param admin 修改用户
     */
    @AutoFile(OperationType.UPDATE)
    void updateAdmin(Admin admin);

    /**
     * 删除管理员用户
     * @param ids 删除用户id
     */
    void deleteUser(List<Long> ids);

    /**
     * 删除管理员用户
     * @param ids 删除用户id
     */
    void deleteAdmin(List<Long> ids);

    /**
     * 获取管理员用户列表
     * @param adminUserInfoPageDTO 查询参数
     * @return 管理员用户列表
     */
    Page<AdminWithUserInfoPageVO> list(AdminUserInfoPageDTO adminUserInfoPageDTO);

    /**
     * 根据id查询管理员用户详情
     * @param id 管理员用户id
     * @return 管理员用户详情
     */
    AdminWithUserInfoVO selectAdminWithUserInfoById(Long id);

    /**
     * 修改管理员用户状态
     * @param adminUserInfoUpdateStatusDTO 修改用户状态
     */
    void updateUserInfoStatus(AdminUserInfoUpdateStatusDTO adminUserInfoUpdateStatusDTO);

    /**
     * 根据用户名查询管理员用户
     * @param username 用户名
     * @return 管理员用户
     */
    User getAdminByUsername(String username);
}
