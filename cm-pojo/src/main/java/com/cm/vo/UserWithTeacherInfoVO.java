package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithTeacherInfoVO implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

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
     * 性别（1:男, 2:女, 0:未知）
     */
    private Integer gender;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 工号
     */
    private String teacherNo;

    /**
     * 职称
     */
    private String title;

    /**
     * 入职日期
     */
    private LocalDate hireDate;

    /**
     * 在职状态（1:在职, 2:离职, 3:退休）
     */
    private Integer status;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 专业ID
     */
    private Long majorId;

    /**
     * 专业名称
     */
    private String majorName;

}
