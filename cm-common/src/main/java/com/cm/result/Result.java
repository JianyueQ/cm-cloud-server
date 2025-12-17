package com.cm.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @author 31373
 * @param <T>
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    //编码：1成功，0和其它数字为失败
    private Integer code;
    //错误信息
    private String msg;
    //数据
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
