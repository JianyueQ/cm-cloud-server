package com.cm.mapper;

import com.cm.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface AdminLoginMapper {
    /**
     * 获取用户信息
     * @param user 用户信息
     * @return 用户信息
     */
    User getAdminByUser(User user);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getAdminByUsername(String username);
}
