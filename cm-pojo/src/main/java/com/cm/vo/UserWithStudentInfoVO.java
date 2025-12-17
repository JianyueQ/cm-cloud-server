package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithStudentInfoVO implements Serializable {

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
     * 学号
     */
    private String studentNo;

    /**
     * 学历层次（1:专科, 2:本科, 3:研究生）
     */
    private Integer educationLevel;

    /**
     * 学籍状态（1:在读, 2:休学, 3:退学, 4:毕业）
     */
    private Integer status;

    /**
     * 入学年份
     */
    private Integer enrollmentYear;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 专业ID
     */
    private Long majorId;

    /**
     * 班级ID
     */
    private Long classId;

}
