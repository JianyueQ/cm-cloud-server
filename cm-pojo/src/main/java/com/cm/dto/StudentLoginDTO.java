package com.cm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLoginDTO implements Serializable {

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含英文、数字和下划线")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^[^\\s\u4e00-\u9fa5]+$", message = "密码不能包含空格和汉字")
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "验证码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "验证码只能包含英文和数字")
    private String code;

    /**
     * uuid
     */
    @NotNull(message = "uuid不能为空")
    private String uuid;

    /**
     * 验证码标识
     */
    private String flag;

}
