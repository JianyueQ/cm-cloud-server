package com.cm.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentUserInfoVO implements Serializable {

    /**
     * 学生信息
     */
    private StudentVO studentVO;

    /**
     * 用户id
     */
    @JsonIgnore
    private Long id;

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
     * 头像URL
     */
    private String avatar;

    /**
     * 性别（1:男, 2:女, 0:未知）
     */
    private Integer gender;

    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

    /**
     * 用户类型（1:学生, 2:教师, 3:管理员）
     */
    private Integer userType;

}
