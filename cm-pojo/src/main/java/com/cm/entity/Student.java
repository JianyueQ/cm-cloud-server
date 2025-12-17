package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生信息实体类
 * 对应数据库表: student
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    
    /**
     * 学生ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 学号
     */
    private String studentNo;
    
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
    
    /**
     * 入学年份
     */
    private Integer enrollmentYear;
    
    /**
     * 年级
     */
    private String grade;
    
    /**
     * 学历层次（1:专科, 2:本科, 3:研究生）
     */
    private Integer educationLevel;
    
    /**
     * 学籍状态（1:在读, 2:休学, 3:退学, 4:毕业）
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