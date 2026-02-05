package com.cm.common.core.Interceptor;


import com.cm.common.core.context.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户信息拦截器
 *
 * @author 31373
 */
@Slf4j
@Component
public class UserInfoInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取登录用户信息
        String userId = request.getHeader("user-id");
        if (StringUtils.isNotEmpty(userId)) {
            log.info("用户id为：{}", userId);
            BaseContext.setCurrentId(Long.valueOf(userId));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("清理的用户id:{}", BaseContext.getCurrentId());
        BaseContext.removeCurrentId();
    }
}
