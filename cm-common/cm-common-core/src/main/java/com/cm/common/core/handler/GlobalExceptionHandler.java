package com.cm.common.core.handler;



import com.cm.common.core.constant.MessageConstant;
import com.cm.common.core.exception.BaseException;
import com.cm.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 * @author 31373
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String UNKNOWN_ERROR = "未知错误";


    /**
     * 捕获业务异常
     * @param ex 业务异常
     * @return 错误信息
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理sql异常
     * @param ex sql异常
     * @return 错误信息
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
//        Duplicate entry 'yuxia12' for key 'employee.idx_username'
        String message = ex.getMessage();
        //如果包含Duplicate entry
        if (message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            log.error(message);
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else {
            log.error(message);
            return Result.error(UNKNOWN_ERROR);
        }
    }

    /**
     * 参数验证失败异常
     * @param ex 参数验证失败异常
     * @return 错误信息
     */
    @ExceptionHandler
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.warn("参数验证失败异常：{}", ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Result.error(errorMessage);
    }
}
