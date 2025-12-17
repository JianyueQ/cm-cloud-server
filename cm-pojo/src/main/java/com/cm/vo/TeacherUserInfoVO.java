package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class TeacherUserInfoVO implements Serializable {

    /**
     * 教师id
     */
    private Long teacherId;

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
     * 用户类型（1:学生, 2:教师, 3:管理员）
     */
    private Integer userType;

    /**
     * 工号
     */
    private String teacherNo;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 专业名称
     */
    private String majorName;

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

}
