package com.cm.service.serviceImpl;

import com.cm.constant.*;
import com.cm.dto.TeacherLoginDTO;
import com.cm.entity.User;
import com.cm.exception.LoginQuestionException;
import com.cm.mapper.TeacherLoginMapper;
import com.cm.properties.JwtProperties;
import com.cm.service.TeacherLoginService;
import com.cm.service.VerifyCodeService;
import com.cm.utils.JwtUtil;
import com.cm.vo.TeacherLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TeacherLoginServiceImpl implements TeacherLoginService {

    @Autowired
    private TeacherLoginMapper teacherLoginMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 教师登录
     *
     * @param teacherLoginDTO 管理员登录参数
     * @return token
     */
    @Override
    public TeacherLoginVO login(TeacherLoginDTO teacherLoginDTO) {
        //查看redis登录计数器,判断是否大于等于5,如果满足条件则锁定用户5分钟
        String loginCount = redisTemplate.opsForValue().get(RedisConstant.ADMIN_LOGIN_ERROR_COUNT_KEY + teacherLoginDTO.getUsername());
        if (loginCount != null && Integer.parseInt(loginCount) >= 5) {
            throw new LoginQuestionException(MessageConstant.USER_LOCKED);
        }
        //根据用户名查询用户信息
        User user = new User();
        user.setUsername(teacherLoginDTO.getUsername());
        user = teacherLoginMapper.getTeacherByUser(user);
        //校验用户
        validate(user);
        //密码比对
        String password = DigestUtils.md5DigestAsHex(teacherLoginDTO.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            //如果密码输入错误超过五次就锁定账户5分钟
            redisTemplate.opsForValue().increment(RedisConstant.STUDENT_LOGIN_ERROR_COUNT_KEY + teacherLoginDTO.getUsername(), RedisConstant.LOGIN_ERROR_COUNT);
            //设置时间
            redisTemplate.expire(RedisConstant.STUDENT_LOGIN_ERROR_COUNT_KEY + teacherLoginDTO.getUsername(), RedisConstant.LOGIN_ERROR_COUNT_TIME, TimeUnit.MINUTES);
            //密码错误
            throw new LoginQuestionException(MessageConstant.PASSWORD_ERROR);
        }
        //验证码验证
        boolean checkVerifyCode = verifyCodeService.checkVerifyCode(teacherLoginDTO.getUuid(), teacherLoginDTO.getCode());
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
        return TeacherLoginVO.builder()
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
        if (!TypeConstant.USER_TYPE_TEACHER.equals(user.getUserType())){
            //用户不是教师
            throw new LoginQuestionException(MessageConstant.USER_NOT_TEACHER);
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
        return map;
    }
}
