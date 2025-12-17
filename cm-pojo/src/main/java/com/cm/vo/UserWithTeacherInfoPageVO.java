package com.cm.vo;

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
public class UserWithTeacherInfoPageVO implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 教师id
     */
    private Long teacherId;

    /**
     * 用户名
     */
    private String username;

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
     * 工号
     */
    private String teacherNo;

    /**
     * 职称
     */
    private String title;

}
