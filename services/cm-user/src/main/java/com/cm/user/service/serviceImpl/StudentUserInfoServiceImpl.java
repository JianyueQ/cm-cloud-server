package com.cm.user.service.serviceImpl;

import com.cm.common.core.constant.ParametersQuestionConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.exception.ParametersQuestionException;
import com.cm.dto.ResetPasswordDTO;
import com.cm.dto.StudentUserInfoUpdateDTO;
import com.cm.entity.User;
import com.cm.user.mapper.StudentUserInfoMapper;
import com.cm.user.service.StudentUserInfoService;
import com.cm.vo.StudentUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author 31373
 */
@Slf4j
@Service
public class StudentUserInfoServiceImpl implements StudentUserInfoService {

    @Autowired
    private StudentUserInfoMapper studentUserInfoMapper;

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @Override
    public StudentUserInfoVO getUserInfo() {
        Long currentId = BaseContext.getCurrentId();
        log.info("当前用户id为：{}", currentId);
        return studentUserInfoMapper.getUserInfoById(currentId);
    }

    /**
     * 修改用户信息
     * @param studentUserInfoUpdateDTO 修改用户信息
     */
    @Override
    public void updateUserInfo(StudentUserInfoUpdateDTO studentUserInfoUpdateDTO) {
        Long currentId = BaseContext.getCurrentId();
        User user = new User();
        BeanUtils.copyProperties(studentUserInfoUpdateDTO, user);
        user.setId(currentId);
        studentUserInfoMapper.updateUserInfo(user);
    }

    /**
     * 修改密码
     * @param resetPasswordDTO 修改密码
     */
    @Override
    public void updatePassword(ResetPasswordDTO resetPasswordDTO) {
        Long currentId = BaseContext.getCurrentId();
        checkUserPassword(resetPasswordDTO);
        //查询旧密码
        User userInfo = studentUserInfoMapper.selectById(currentId);
        //验证密码,使用MD5加密比对
        String encodedPassword = DigestUtils.md5DigestAsHex(resetPasswordDTO.getOldPassword().getBytes());
        if (!userInfo.getPassword().equals(encodedPassword)) {
            throw new ParametersQuestionException(ParametersQuestionConstant.PASSWORD_ERROR);
        }
        //更新密码
        userInfo.setPassword(DigestUtils.md5DigestAsHex(resetPasswordDTO.getNewPassword().getBytes()));
        studentUserInfoMapper.updateUserInfo(userInfo);
    }

    /**
     * 验证用户密码
     * @param resetPasswordDTO 修改密码
     */
    private void checkUserPassword(ResetPasswordDTO resetPasswordDTO) {
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getRePassword())){
            //新密码与确认密码不一致
            throw new ParametersQuestionException(ParametersQuestionConstant.NEW_PASSWORD_SAME_AS_RE_PASSWORD);
        }
        if (resetPasswordDTO.getOldPassword().equals(resetPasswordDTO.getNewPassword())){
            //新密码与旧密码一致
            throw new ParametersQuestionException(ParametersQuestionConstant.NEW_PASSWORD_SAME_AS_OLD_PASSWORD);
        }
    }
}
