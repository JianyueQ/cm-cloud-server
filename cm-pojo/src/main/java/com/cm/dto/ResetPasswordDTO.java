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
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO implements Serializable {

    /**
     * 旧密码
     */
    //旧密码格式中不能含有空格和汉字
    @Pattern(regexp = "^[^\\s\u4e00-\u9fa5]+$", message = "旧密码不能包含空格和汉字")
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */

    @NotNull(message = "新密码不能为空")
    @Pattern(regexp = "^[^\\s\u4e00-\u9fa5]+$", message = "新密码不能包含空格和汉字")
    private String newPassword;
    /**
     * 重复新密码
     */

    @NotNull(message = "确认新密码不能为空")
    @Pattern(regexp = "^[^\\s\u4e00-\u9fa5]+$", message = "确认新密码不能包含空格和汉字")
    private String rePassword;

}
