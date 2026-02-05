package com.cm.common.aop.aspect;

import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.core.constant.AutoFileConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 公共字段填充
 *
 * @author 31373
 */
@Component
@Slf4j
@Aspect
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AutoFileAspect {


    //切点
//    @Pointcut("execution(* com.cm.*.mapper.*.*(..)) && @annotation(com.cm.annotations.AutoFile)")
    @Pointcut("@annotation(com.cm.common.aop.annotations.AutoFile)")
    public void autoFilePointCut() {}

    //前置通知
    @Before("autoFilePointCut()")
    public void autoFile(JoinPoint joinPoint) {
        log.info("开始填充公共字段.....");
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的对象
        AutoFile autoFile = signature.getMethod().getAnnotation(AutoFile.class);
        //获取注解中的内容
        OperationType operationType = null;
        if (autoFile != null) {
            operationType = autoFile.value();
        }
        //获取方法参数
        Object[] args = joinPoint.getArgs();

        //约定:只要第一个参数是实体类,就填充
        if (args == null || args.length == 0) {
            return;
        }
        Object object = args[0];

        //创建人,修改人,创建时间,修改时间
        Long currentId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateUser = object.getClass().getDeclaredMethod(AutoFileConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFileConstant.SET_UPDATE_USER, Long.class);
                Method setCreateTime = object.getClass().getDeclaredMethod(AutoFileConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFileConstant.SET_UPDATE_TIME, LocalDateTime.class);

                setCreateUser.invoke(object, currentId);
                setUpdateUser.invoke(object, currentId);
                setCreateTime.invoke(object, now);
                setUpdateTime.invoke(object, now);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFileConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFileConstant.SET_UPDATE_TIME, LocalDateTime.class);

                setUpdateUser.invoke(object, currentId);
                setUpdateTime.invoke(object, now);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

    }

}
