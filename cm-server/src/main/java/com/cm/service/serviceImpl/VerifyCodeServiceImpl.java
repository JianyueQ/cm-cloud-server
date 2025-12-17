package com.cm.service.serviceImpl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.lang.UUID;
import com.cm.constant.MessageConstant;
import com.cm.constant.RedisConstant;
import com.cm.exception.LoginQuestionException;
import com.cm.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Service
@Slf4j
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成验证码
     * @return  map
     */
    @Override
    public Map<String, String> generateVerifyCode() {

        String uuid = UUID.randomUUID().toString();
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100);
        //图形验证码写出，可以写出到文件，也可以写出到流
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.write(os);
        String code = captcha.getCode();
        log.info("验证码：{},uuid:{}", code, uuid);
        //存入redis
        ConcurrentHashMap<Object, Object> hashMap = new ConcurrentHashMap<>(5);
        hashMap.put(uuid,code);
        redisTemplate.opsForHash().putAll(RedisConstant.REDIS_KEY_CODE + uuid, hashMap);
        //设置过期时间
        redisTemplate.expire(RedisConstant.REDIS_KEY_CODE + uuid, RedisConstant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        Map<String, String> map = new ConcurrentHashMap<>(5);
        map.put("uuid", uuid);
        map.put("image", Base64.encode(os.toByteArray()));
        return map;
    }

    /**
     * 刷新验证码
     * @param uuid uuid
     * @return 验证码
     */
    @Override
    public String generateVerifyCodeByUuid(String uuid) {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100);
        //图形验证码写出，可以写出到文件，也可以写出到流
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.write(os);
        String code = captcha.getCode();
        log.info("验证码：{},UUID:{}", code, uuid);
        //存入redis
        ConcurrentHashMap<Object, Object> hashMap = new ConcurrentHashMap<>(5);
        hashMap.put(uuid,code);
        redisTemplate.opsForHash().putAll(RedisConstant.REDIS_KEY_CODE + uuid, hashMap);
        //设置过期时间
        redisTemplate.expire(RedisConstant.REDIS_KEY_CODE + uuid, RedisConstant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return Base64.encode(os.toByteArray());
    }

    /**
     * 校验验证码
     * @param uuid uuid
     * @param code 验证码
     * @return 校验成功 返回true/校验失败 抛出异常 返回false
     */
    @Override
    public boolean checkVerifyCode(String uuid, String code) {
        //redis验证码
        String redisCode = (String) redisTemplate.opsForHash().get(RedisConstant.REDIS_KEY_CODE + uuid, uuid);
        if (redisCode == null) {
            //验证码已经过期
            throw new LoginQuestionException(MessageConstant.LOGIN_CODE_EXPIRED);
        }
        // 严格匹配数字
        boolean result = redisCode.equals(code);
        if(result) {
            // 一次性使用
            redisTemplate.delete(RedisConstant.REDIS_KEY_CODE + uuid);
        }
        return result;
    }


}
