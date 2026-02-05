package com.cm.auth.service.serviceImpl;

import com.cm.auth.mapper.StudentLoginMapper;
import com.cm.auth.service.StudentLoginService;
import com.cm.auth.service.VerifyCodeService;

import com.cm.common.core.constant.*;
import com.cm.common.core.exception.LoginQuestionException;
import com.cm.common.core.properties.JwtProperties;
import com.cm.common.core.utils.JwtUtil;
import com.cm.dto.StudentLoginDTO;
import com.cm.entity.User;
import com.cm.vo.StudentLoginVO;
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
public class StudentLoginServiceImpl implements StudentLoginService {

    private final StudentLoginMapper studentLoginMapper;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final VerifyCodeService verifyCodeService;

    public StudentLoginServiceImpl(StudentLoginMapper studentLoginMapper, JwtProperties jwtProperties, RedisTemplate<String, String> redisTemplate, VerifyCodeService verifyCodeService) {
        this.studentLoginMapper = studentLoginMapper;
        this.jwtProperties = jwtProperties;
        this.redisTemplate = redisTemplate;
        this.verifyCodeService = verifyCodeService;
    }

    /**
     * 学生登录
     *
     * @param studentLoginDTO 学生登录参数
     * @return
     */
    @Override
    public StudentLoginVO login(StudentLoginDTO studentLoginDTO) {
        //查看redis登录计数器,判断是否大于等于5,如果满足条件则锁定用户5分钟
        String loginCount = redisTemplate.opsForValue().get(RedisConstant.STUDENT_LOGIN_ERROR_COUNT_KEY + studentLoginDTO.getUsername());
        if (loginCount != null && Integer.parseInt(loginCount) >= 5) {
            throw new LoginQuestionException(MessageConstant.USER_LOCKED);
        }
        //根据用户名查询用户信息
        User user = new User();
        user.setUsername(studentLoginDTO.getUsername());
        user = studentLoginMapper.getStudentByUser(user);
        //校验用户
        validate(user);
        //密码比对
        String password = DigestUtils.md5DigestAsHex(studentLoginDTO.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            //如果密码输入错误超过五次就锁定账户5分钟
            redisTemplate.opsForValue().increment(RedisConstant.STUDENT_LOGIN_ERROR_COUNT_KEY + studentLoginDTO.getUsername(), RedisConstant.LOGIN_ERROR_COUNT);
            //设置时间
            redisTemplate.expire(RedisConstant.STUDENT_LOGIN_ERROR_COUNT_KEY + studentLoginDTO.getUsername(), RedisConstant.LOGIN_ERROR_COUNT_TIME, TimeUnit.MINUTES);
            //密码错误
            throw new LoginQuestionException(MessageConstant.PASSWORD_ERROR);
        }
        //获取验证码验证,如果携带验证码验证,就跳过验证码校验的步骤
        if (studentLoginDTO.getFlag() == null || !studentLoginDTO.getFlag().equals(FlagConstant.FLAG_VERIFY_CODE)) {
            //验证码验证
            boolean checkVerifyCode = verifyCodeService.checkVerifyCode(studentLoginDTO.getUuid(), studentLoginDTO.getCode());
            if (!checkVerifyCode){
                throw new LoginQuestionException(MessageConstant.LOGIN_CODE_ERROR);
            }
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
        return StudentLoginVO.builder()
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
        if (!TypeConstant.USER_TYPE_STUDENT.equals(user.getUserType())){
            //用户不是学生
            throw new LoginQuestionException(MessageConstant.USER_NOT_STUDENT);
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
