package com.cm.common.core.exception;

/**
 * 业务异常
 * @author 31373
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
