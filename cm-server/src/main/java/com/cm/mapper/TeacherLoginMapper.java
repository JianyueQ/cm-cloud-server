package com.cm.mapper;

import com.cm.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface TeacherLoginMapper {
    /**
     * 获取用户信息
     * @param user 用户信息
     * @return 用户信息
     */
    User getTeacherByUser(User user);
}
