package com.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

    /**
     * 职位
     */
    private String position;

    /**
     * 入职日期
     */
    private LocalDate hireDate;

}
