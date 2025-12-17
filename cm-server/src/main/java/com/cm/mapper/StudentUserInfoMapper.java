package com.cm.mapper;

import com.cm.entity.User;
import com.cm.vo.StudentUserInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface StudentUserInfoMapper {
    /**
     * 获取用户信息
     * @param currentId 当前用户id
     * @return 用户信息
     */
    StudentUserInfoVO getUserInfoById(Long currentId);

    /**
     * 修改用户信息
     * @param user 修改用户信息
     */
    void updateUserInfo(User user);

    /**
     * 根据id查询用户信息
     * @param currentId 当前用户id
     * @return 用户信息
     */
    User selectById(Long currentId);
}
