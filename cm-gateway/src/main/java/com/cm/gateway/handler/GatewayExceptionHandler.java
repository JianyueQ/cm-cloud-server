package com.cm.gateway.handler;



import com.cm.common.core.constant.MessageConstant;
import com.cm.common.core.exception.BaseException;
import com.cm.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 网关异常处理
 * @author 31373
 */
@RestControllerAdvice
@Slf4j
public class GatewayExceptionHandler {


    /**
     * 捕获业务异常
     * @param ex 业务异常
     * @return 错误信息
     */
    @ExceptionHandler({ConnectException.class,})
    public Result<String> exceptionHandler(ConnectException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(MessageConstant.SERVICE_NOT_FOUND);
    }


}
