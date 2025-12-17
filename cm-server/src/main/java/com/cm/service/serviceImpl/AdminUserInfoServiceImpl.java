package com.cm.service.serviceImpl;

import cn.hutool.core.util.PhoneUtil;
import com.cm.annotations.RedisCache;
import com.cm.annotations.RedisCacheEvict;
import com.cm.constant.MapConstant;
import com.cm.constant.ParametersQuestionConstant;
import com.cm.constant.RedisConstant;
import com.cm.constant.TypeConstant;
import com.cm.context.BaseContext;
import com.cm.dto.*;
import com.cm.entity.Admin;
import com.cm.entity.User;
import com.cm.enumeration.OperationType;
import com.cm.exception.ParametersQuestionException;
import com.cm.mapper.AdminLoginMapper;
import com.cm.mapper.AdminUserInfoMapper;
import com.cm.result.PageResult;
import com.cm.service.AdminUserInfoService;
import com.cm.utils.ParametersUtil;
import com.cm.utils.UUIDUtils;
import com.cm.vo.AdminUserInfoVO;
import com.cm.vo.AdminWithUserInfoPageVO;
import com.cm.vo.AdminWithUserInfoVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Slf4j
@Service
public class AdminUserInfoServiceImpl implements AdminUserInfoService {

    @Autowired
    private AdminUserInfoMapper adminUserInfoMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AdminLoginMapper adminLoginMapper;

    private static final String TRUE = "true";

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @Override
    public AdminUserInfoVO getUserInfo() {
        Long currentId = BaseContext.getCurrentId();
        log.info("当前用户id为：{}", currentId);
        AdminUserInfoVO adminUserInfoVO = adminUserInfoMapper.getUserInfoById(currentId);
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        adminUserInfoVO.setSuperAdmin(map.get(RedisConstant.HASH_KEY_SUPER_ADMIN).toString());
        return adminUserInfoVO;
    }

    /**
     * 修改用户信息
     * @param adminUserInfoUpdateDTO 修改用户信息
     */
    @Override
    public void updateUserInfo(AdminUserInfoUpdateDTO adminUserInfoUpdateDTO) {
        Long currentId = BaseContext.getCurrentId();
        checkParam(adminUserInfoUpdateDTO);
        User user = new User();
        BeanUtils.copyProperties(adminUserInfoUpdateDTO, user);
        user.setId(currentId);
        adminUserInfoMapper.updateUserInfo(user);
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
        User userInfo = adminUserInfoMapper.selectById(currentId);
        //验证密码,使用MD5加密比对
        String encodedPassword = DigestUtils.md5DigestAsHex(resetPasswordDTO.getOldPassword().getBytes());
        if (!userInfo.getPassword().equals(encodedPassword)) {
            throw new ParametersQuestionException(ParametersQuestionConstant.PASSWORD_ERROR);
        }
        //更新密码
        userInfo.setPassword(DigestUtils.md5DigestAsHex(resetPasswordDTO.getNewPassword().getBytes()));
        adminUserInfoMapper.updateUserInfo(userInfo);

    }

    /**
     * 添加管理员用户
     *
     * @param adminUserInfoDTO 添加用户
     */
    @RedisCacheEvict({@RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:list:", isPattern = true)})
    @Transactional
    @Override
    public void addUser(AdminUserInfoDTO adminUserInfoDTO) {
        Long currentId = BaseContext.getCurrentId();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        if (!TRUE.equals(map.get(RedisConstant.HASH_KEY_SUPER_ADMIN).toString())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.NO_PERMISSION);
        }
        //校验参数
        checkAddOrUpdateUser(adminUserInfoDTO, OperationType.INSERT);
        //查询用户名是否重复
        User repeat = adminLoginMapper.getAdminByUsername(adminUserInfoDTO.getUsername());
        if (repeat != null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USERNAME_EXIST);
        }
        User user = new User();
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminUserInfoDTO, user);
        BeanUtils.copyProperties(adminUserInfoDTO, admin);
        //密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //设置工号
        admin.setAdminNo(UUIDUtils.generateJobNumberWithDateAndUUID());
        //设置性别
        user.setGender(0);
        //设置用户类型
        user.setUserType(TypeConstant.USER_TYPE_ADMIN);
        //设置状态
        user.setStatus(1);
        adminUserInfoMapper.insertUser(user);
        admin.setUserId(user.getId());
        adminUserInfoMapper.insertAdmin(admin);
    }

    /**
     * 修改管理员用户
     *
     * @param adminUserInfoDTO 修改用户
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:details:", keyParts = "#adminUserInfoDTO.id")
    })
    @Override
    public void updateUser(AdminUserInfoDTO adminUserInfoDTO) {
        Long currentId = BaseContext.getCurrentId();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        if (!TRUE.equals(map.get(RedisConstant.HASH_KEY_SUPER_ADMIN).toString())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.NO_PERMISSION);
        }
        //校验参数
        checkAddOrUpdateUser(adminUserInfoDTO, OperationType.UPDATE);
        User user = new User();
        BeanUtils.copyProperties(adminUserInfoDTO, user);
        adminUserInfoMapper.updateUserInfo(user);
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminUserInfoDTO, admin);
        admin.setUserId(adminUserInfoDTO.getId());
        adminUserInfoMapper.updateAdmin(admin);
    }

    /**
     * 删除管理员用户
     *
     * @param ids 删除用户id
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:details:", keyParts = "#ids")
    })
    @Transactional
    @Override
    public void deleteUser(List<Long> ids) {
        Long currentId = BaseContext.getCurrentId();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        if (!TRUE.equals(map.get(RedisConstant.HASH_KEY_SUPER_ADMIN).toString())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.NO_PERMISSION);
        }
        adminUserInfoMapper.deleteUser(ids);
        adminUserInfoMapper.deleteAdmin(ids);
    }

    /**
     * 管理员用户-分页查询
     *
     * @param adminUserInfoPageDTO 管理员用户-分页查询
     * @return 管理员用户-分页查询
     */
    @RedisCache(keyPrefix = "adminUserInfo:list:", keyParts = {
            "#adminUserInfoPageDTO.pageNum", "#adminUserInfoPageDTO.pageSize",
            "#adminUserInfoPageDTO.username", "#adminUserInfoPageDTO.status",
            "#adminUserInfoPageDTO.realName", "#adminUserInfoPageDTO.adminNo"
    },expireTime = 1,timeUnit = TimeUnit.HOURS)
    @Override
    public PageResult list(AdminUserInfoPageDTO adminUserInfoPageDTO) {
        PageHelper.startPage(adminUserInfoPageDTO.getPageNum(), adminUserInfoPageDTO.getPageSize());
        Page<AdminWithUserInfoPageVO> page = adminUserInfoMapper.list(adminUserInfoPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 获取管理员用户详情
     *
     * @param id 管理员用户id
     * @return 管理员用户
     */
    @RedisCache(keyPrefix = "adminUserInfo:details:", keyParts = "#id",expireTime = 1,timeUnit = TimeUnit.HOURS)
    @Override
    public AdminWithUserInfoVO get(Long id) {
        return adminUserInfoMapper.selectAdminWithUserInfoById(id);
    }

    /**
     * 重置密码
     *
     * @return 重置密码
     */
    @Override
    public String resetPwd(Long id) {
        //根据id查询用户
        User user = adminUserInfoMapper.selectById(id);
        if (user == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USER_NOT_EXIST);
        }
        String password = RandomStringUtils.randomAlphanumeric(11);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        adminUserInfoMapper.updateUserInfo(user);
        return password;
    }

    /**
     * 修改状态
     *
     * @param adminUserInfoUpdateStatusDTO 修改状态
     */
    @RedisCacheEvict({
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:list:", isPattern = true),
            @RedisCacheEvict.CacheKeyConfig(keyPrefix = "adminUserInfo:details:", keyParts = "#adminUserInfoUpdateStatusDTO.id")
    })
    @Override
    public void updateStatus(AdminUserInfoUpdateStatusDTO adminUserInfoUpdateStatusDTO) {
        Long currentId = BaseContext.getCurrentId();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        if (!TRUE.equals(map.get(RedisConstant.HASH_KEY_SUPER_ADMIN).toString())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.NO_PERMISSION);
        }
        adminUserInfoMapper.updateUserInfoStatus(adminUserInfoUpdateStatusDTO);
        //删除redis中存储的用户信息
        Map<Object, Object> existingUserSession = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + adminUserInfoUpdateStatusDTO.getId());
        if (!existingUserSession.isEmpty()) {
            Object existingToken = existingUserSession.get(MapConstant.USER_TOKEN);
            if (existingToken != null) {
                // 删除旧的token
                redisTemplate.delete(RedisConstant.JWT_TOKEN_KEY + existingToken);
            }
        }
    }

    /**
     * 密码校验
     */
    private void checkUserPassword(ResetPasswordDTO resetPasswordDTO) {
        if (resetPasswordDTO == null){
            throw new ParametersQuestionException(ParametersQuestionConstant.PARAMETERS_NOT_NULL);
        }
        if (resetPasswordDTO.getOldPassword() == null) {
            //旧密码为空
            throw new ParametersQuestionException(ParametersQuestionConstant.OLD_PASSWORD_NOT_NULL);
        }
        if (resetPasswordDTO.getNewPassword() == null){
            //新密码为空
            throw new ParametersQuestionException(ParametersQuestionConstant.NEW_PASSWORD_NOT_NULL);
        }
        if (resetPasswordDTO.getRePassword() == null){
            //确认密码为空
            throw new ParametersQuestionException(ParametersQuestionConstant.RE_PASSWORD_NOT_NULL);
        }
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getRePassword())){
            //新密码与确认密码不一致
            throw new ParametersQuestionException(ParametersQuestionConstant.NEW_PASSWORD_SAME_AS_RE_PASSWORD);
        }
        if (resetPasswordDTO.getOldPassword().equals(resetPasswordDTO.getNewPassword())){
            //新密码与旧密码一致
            throw new ParametersQuestionException(ParametersQuestionConstant.NEW_PASSWORD_SAME_AS_OLD_PASSWORD);
        }
    }


    /**
     * 非空校验,和参数格式校验
     */
    private void checkParam(AdminUserInfoUpdateDTO adminUserInfoUpdateDTO) {
        if (adminUserInfoUpdateDTO.getAvatar() != null){
            return;
        }
        if (adminUserInfoUpdateDTO.getEmail() == null){
            // 邮箱不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.EMAIL_NOT_NULL);
        }
        //邮箱格式正则校验
        if (!ParametersUtil.isEmail(adminUserInfoUpdateDTO.getEmail())){
            //邮箱格式不正确
            throw new ParametersQuestionException(ParametersQuestionConstant.EMAIL_ERROR);
        }
        if (adminUserInfoUpdateDTO.getPhone()== null){
            // 手机号不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.PHONE_NOT_NULL);
        }
        if (!PhoneUtil.isPhone(adminUserInfoUpdateDTO.getPhone())){
            //手机号格式不正确
            throw new ParametersQuestionException(ParametersQuestionConstant.PHONE_ERROR);
        }
        if (adminUserInfoUpdateDTO.getGender() == null){
            //性别不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.GENDER_NOT_NULL);
        }
        if (adminUserInfoUpdateDTO.getRealName()== null){
            //真实姓名不能为空
            throw new ParametersQuestionException(ParametersQuestionConstant.REAL_NAME_NOT_NULL);
        }
        //真实姓名不能包含数字或者字母以及其他标点符号
        if (!ParametersUtil.isRealName(adminUserInfoUpdateDTO.getRealName())){
            throw new ParametersQuestionException(ParametersQuestionConstant.REAL_NAME_NOT_RIGHT);
        }
    }

    /**
     * 添加或者修改管理员用户信息的校验
     */
    private void checkAddOrUpdateUser(AdminUserInfoDTO adminUserInfoDTO, OperationType operationType) {
        //用户名不能为空
        if (adminUserInfoDTO.getUsername() == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USERNAME_NOT_NULL);
        }
        //用户名只能是字母+数字
        if (!ParametersUtil.isUsername(adminUserInfoDTO.getUsername())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.USERNAME_NOT_RIGHT);
        }
        //密码不能为空
        if (adminUserInfoDTO.getPassword() == null && operationType == OperationType.INSERT) {
            throw new ParametersQuestionException(ParametersQuestionConstant.PASSWORD_NOT_NULL);
        }
        //密码不能含有空格和汉字
        if (adminUserInfoDTO.getPassword() != null && operationType == OperationType.INSERT) {
            // 检查是否包含空格
            if (adminUserInfoDTO.getPassword().contains(" ")) {
                throw new ParametersQuestionException(ParametersQuestionConstant.PASSWORD_NOT_RIGHT);
            }
            // 检查是否包含汉字
            for (char c : adminUserInfoDTO.getPassword().toCharArray()) {
                if (Character.isIdeographic(c)) {
                    throw new ParametersQuestionException(ParametersQuestionConstant.PASSWORD_NOT_RIGHT);
                }
            }
        }
        //真实姓名不能为空
        if (adminUserInfoDTO.getRealName() == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.REAL_NAME_NOT_NULL);
        }
        //真实姓名不能包含数字或者字母以及其他标点符号
        if (!ParametersUtil.isRealName(adminUserInfoDTO.getRealName())) {
            throw new ParametersQuestionException(ParametersQuestionConstant.REAL_NAME_NOT_RIGHT);
        }
        //手机号格式不正确
        if (adminUserInfoDTO.getPhone() != null) {
            if (!PhoneUtil.isPhone(adminUserInfoDTO.getPhone())) {
                throw new ParametersQuestionException(ParametersQuestionConstant.PHONE_ERROR);
            }
        }
        if (adminUserInfoDTO.getEmail() != null) {
            //邮箱格式正则校验
            if (!ParametersUtil.isEmail(adminUserInfoDTO.getEmail())) {
                //邮箱格式不正确
                throw new ParametersQuestionException(ParametersQuestionConstant.EMAIL_ERROR);
            }
        }
        //职位不能为空
        if (adminUserInfoDTO.getPosition() == null) {
            throw new ParametersQuestionException(ParametersQuestionConstant.POSITION_NOT_NULL);
        }
    }
}
