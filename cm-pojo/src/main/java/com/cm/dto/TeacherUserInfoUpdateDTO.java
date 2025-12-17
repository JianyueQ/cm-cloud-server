package com.cm.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUserInfoUpdateDTO implements Serializable {

    /**
     * 头像
     */
    private String avatar;

    /**
     * 真实姓名
     */
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_-]{2,10}$", message = "真实姓名格式错误")
    private String realName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "邮箱格式错误")
    private String email;

    /**
     * 性别（1:男, 2:女, 0:未知）
     */
    @Range(min = 0, max = 2, message = "性别格式错误")
    private Integer gender;

}
