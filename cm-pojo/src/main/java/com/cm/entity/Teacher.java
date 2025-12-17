package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师信息实体类
 * 对应数据库表: teacher
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable {
    /**
     * 教师ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 真实姓名(冗余)
     */
    private String realName;

    /**
     * 工号
     */
    private String teacherNo;
    
    /**
     * 学院ID
     */
    private Long collegeId;
    
    /**
     * 专业ID
     */
    private Long majorId;
    
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
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建人ID
     */
    private Long createUser;
    
    /**
     * 更新人ID
     */
    private Long updateUser;
    
    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDeleted;
}