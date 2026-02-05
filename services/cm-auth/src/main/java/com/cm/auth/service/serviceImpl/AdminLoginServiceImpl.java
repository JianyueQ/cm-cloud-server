package com.cm.auth.service.serviceImpl;

import com.cm.auth.mapper.AdminLoginMapper;
import com.cm.auth.service.AdminLoginService;
import com.cm.auth.service.VerifyCodeService;

import com.cm.common.core.constant.*;
import com.cm.common.core.exception.LoginQuestionException;
import com.cm.common.core.properties.JwtProperties;
import com.cm.common.core.utils.JwtUtil;
import com.cm.dto.AdminLoginDTO;
import com.cm.entity.User;
import com.cm.vo.AdminLoginVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    private final AdminLoginMapper adminLoginMapper;
    private final VerifyCodeService verifyCodeService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;

    public AdminLoginServiceImpl(AdminLoginMapper adminLoginMapper, VerifyCodeService verifyCodeService, RedisTemplate<String, String> redisTemplate, JwtProperties jwtProperties) {
        this.adminLoginMapper = adminLoginMapper;
        this.verifyCodeService = verifyCodeService;
        this.redisTemplate = redisTemplate;
        this.jwtProperties = jwtProperties;
    }


    /**
     * 管理员登录
     * @param adminLoginDTO 管理员登录参数
     * @return token
     */
    @Override
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO) {
        //查看redis登录计数器,判断是否大于等于5,如果满足条件则锁定用户5分钟
        String loginCount = redisTemplate.opsForValue().get(RedisConstant.ADMIN_LOGIN_ERROR_COUNT_KEY + adminLoginDTO.getUsername());
        if (loginCount != null && Integer.parseInt(loginCount) >= 5) {
            throw new LoginQuestionException(MessageConstant.USER_LOCKED);
        }
        //根据用户名查询用户信息
        User user = new User();
        user.setUsername(adminLoginDTO.getUsername());
        user = adminLoginMapper.getAdminByUser(user);
        //校验用户
        validate(user);
        //密码比对
        String password = DigestUtils.md5DigestAsHex(adminLoginDTO.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            //如果密码输入错误超过五次就锁定账户5分钟
            redisTemplate.opsForValue().increment(RedisConstant.ADMIN_LOGIN_ERROR_COUNT_KEY + adminLoginDTO.getUsername(), RedisConstant.ADMIN_LOGIN_ERROR_COUNT);
            //设置时间
            redisTemplate.expire(RedisConstant.ADMIN_LOGIN_ERROR_COUNT_KEY + adminLoginDTO.getUsername(), RedisConstant.ADMIN_LOGIN_ERROR_COUNT_TIME, TimeUnit.MINUTES);
            //密码错误
            throw new LoginQuestionException(MessageConstant.PASSWORD_ERROR);
        }
        //验证码验证
        boolean checkVerifyCode = verifyCodeService.checkVerifyCode(adminLoginDTO.getUuid(), adminLoginDTO.getCode());
        if (!checkVerifyCode){
            throw new LoginQuestionException(MessageConstant.LOGIN_CODE_ERROR);
        }
        // 实现单点登录：检查用户是否已在其他地方登录，如果已登录则将其踢下线
        Map<Object, Object> existingUserSession = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + user.getId());
        if (!existingUserSession.isEmpty()) {
            Object existingToken = existingUserSession.get(MapConstant.USER_TOKEN);
            if (existingToken != null) {
                // 删除旧的token
                redisTemplate.delete(RedisConstant.JWT_TOKEN_KEY + existingToken);
            }
        }
        //生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.REAL_NAME, user.getRealName());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );
        Map<String, String> map = getStringStringMap(user, token);
        //将token存入redis
        redisTemplate.opsForHash().put(RedisConstant.JWT_TOKEN_KEY + token, MapConstant.ID, user.getId().toString());
        redisTemplate.opsForHash().putAll(RedisConstant.JWT_ID_KEY + user.getId(),map);
        //设置过期时间
        redisTemplate.expire(RedisConstant.JWT_TOKEN_KEY + token, RedisConstant.TOKEN_TTL, TimeUnit.HOURS);
        redisTemplate.expire(RedisConstant.JWT_ID_KEY + user.getId(), RedisConstant.TOKEN_TTL, TimeUnit.HOURS);
        return AdminLoginVO.builder()
                .token(token)
                .build();
    }

    /**
     * 退出登录
     * @param currentId 当前登录管理员id
     */
    @Override
    public void logout(Long currentId) {
        //先查询
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
        //删除redis中的token
        redisTemplate.delete(RedisConstant.JWT_TOKEN_KEY + map.get(MapConstant.USER_TOKEN).toString());
        redisTemplate.delete(RedisConstant.JWT_ID_KEY + currentId);
    }

    /**
     * 用户校验
     */
    private void validate(User user) {
        if (user == null) {
            //用户不存在
            throw new LoginQuestionException(MessageConstant.USER_NOT_EXISTS);
        }
        if (StatusConstant.STATUS_DISABLE.equals(user.getStatus())){
            //用户被禁用
            throw new LoginQuestionException(MessageConstant.USER_DISABLED);
        }
        if (!TypeConstant.USER_TYPE_ADMIN.equals(user.getUserType())){
            //用户不是管理员
            throw new LoginQuestionException(MessageConstant.USER_NOT_ADMIN);
        }
    }

    /**
     * 存储用户的登录信息
     */
    private Map<String, String> getStringStringMap(User user, String token) {
        Map<String, String> map = new HashMap<>();
        map.put(MapConstant.ID, user.getId().toString());
        map.put(MapConstant.USER_TYPE, String.valueOf(user.getUserType()));
        map.put(MapConstant.STATUS, String.valueOf(user.getStatus()));
        map.put(MapConstant.USER_TOKEN, token);
        map.put(MapConstant.REAL_NAME, user.getRealName());
        //如果id=1,username=admin,就标识为超级管理员
        if (user.getId().equals(TypeConstant.ID_SUPER_ADMIN) && user.getUsername().equals(TypeConstant.ADMIN)){
            map.put(MapConstant.SUPER_ADMIN, String.valueOf(true));
        } else if (user.getAdmin().getPosition().equals(TypeConstant.SUPER_ADMIN)) {
            map.put(MapConstant.SUPER_ADMIN, String.valueOf(true));
        } else {
            map.put(MapConstant.SUPER_ADMIN, String.valueOf(false));
        }
        return map;
    }

}
