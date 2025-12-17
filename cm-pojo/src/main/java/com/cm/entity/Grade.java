package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成绩记录实体类
 * 对应数据库表: grade
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade implements Serializable {
    /**
     * 成绩ID
     */
    private Long id;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 授课ID
     */
    private Long courseTeachingId;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 课程名称（冗余）
     */
    private String courseName;
    
    /**
     * 教师ID
     */
    private Long teacherId;
    
    /**
     * 教师姓名（冗余）
     */
    private String teacherName;
    
    /**
     * 学号（冗余）
     */
    private String studentNo;
    
    /**
     * 学生姓名（冗余）
     */
    private String studentName;
    
    /**
     * 班级ID
     */
    private Long classId;
    
    /**
     * 班级名称（冗余）
     */
    private String className;
    
    /**
     * 学分（冗余）
     */
    private BigDecimal credit;
    
    /**
     * 成绩分数
     */
    private BigDecimal score;
    
    /**
     * 成绩等级（A+、A、B+等）
     */
    private String gradeLevel;
    
    /**
     * 绩点
     */
    private BigDecimal gradePoint;
    
    /**
     * 考核方式（1:考试, 2:考查）
     */
    private Integer assessmentType;
    
    /**
     * 学期
     */
    private String semester;
    
    /**
     * 状态（1:正常, 2:补考, 3:重修）
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
     * 创建人ID（教师ID）
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