package com.cm.user.service.serviceImpl;

import com.cm.entity.User;
import com.cm.user.mapper.UserInfoMapper;
import com.cm.user.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * 用户信息Service实现类
 *
 * @author 31373
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }


    @Override
    public User getUserInfoById(Long id) {
        return userInfoMapper.findById(id);
    }
}
