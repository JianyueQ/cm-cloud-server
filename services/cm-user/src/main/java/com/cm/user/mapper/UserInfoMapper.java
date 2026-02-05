package com.cm.user.mapper;

import com.cm.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息Mapper
 * @author 31373
 */
@Mapper
public interface UserInfoMapper {

    User findById(@Param("id") Long id);

}
