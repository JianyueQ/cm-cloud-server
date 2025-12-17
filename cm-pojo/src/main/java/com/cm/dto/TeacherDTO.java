package com.cm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含英文、数字和下划线")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[^\\s\u4e00-\u9fa5]+$", message = "密码不能包含空格和汉字")
    private String password;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Length(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别（1:男, 2:女, 0:未知）
     */
    @NotNull(message = "性别不能为空")
    @Range(min = 0, max = 2, message = "性别值必须在0-2之间")
    private Integer gender;

    /**
     * 学院ID
     */
    @NotNull(message = "学院ID不能为空")
    private Long collegeId;

    /**
     * 专业ID
     */
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    /**
     * 工号
     */
    @NotBlank(message = "工号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "工号只能包含英文和数字")
    private String teacherNo;

    /**
     * 职称
     */
    @NotBlank(message = "职称不能为空")
    private String title;

    /**
     * 入职日期
     */
    @NotNull(message = "入职日期不能为空")
    private LocalDate hireDate;

    /**
     * 在职状态（1:在职, 2:离职, 3:退休）
     */
    @NotNull(message = "在职状态不能为空")
    @Range(min = 1, max = 3, message = "在职状态值必须在1-3之间")
    private Integer status;
}
