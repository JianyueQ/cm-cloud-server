package com.cm.mapper;

import com.cm.entity.User;
import com.cm.vo.TeacherUserInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface TeacherUserInfoMapper {
    /**
     * 根据id查询用户信息
     * @param currentId 当前用户id
     * @return 用户信息
     */
    User selectById(Long currentId);

    /**
     * 修改用户信息
     * @param userInfo 修改用户信息
     */
    void updateUserInfo(User userInfo);

    /**
     * 根据id查询用户信息
     * @param currentId 当前用户id
     * @return 用户信息
     */
    TeacherUserInfoVO getUserInfoById(Long currentId);
}
