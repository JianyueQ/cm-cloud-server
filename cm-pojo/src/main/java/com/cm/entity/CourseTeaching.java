package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程管理实体类（教师授课关系）
 * 对应数据库表: course_teaching
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTeaching implements Serializable {

    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教师ID
     */
    private Long teacherId;
    
    /**
     * 学期（如2023-2024-1）
     */
    private String semester;
    
    /**
     * 授课班级ID集合（逗号分隔）
     */
    private String classIds;
    
    /**
     * 最大学生人数
     */
    private Integer maxStudentCount;
    
    /**
     * 当前学生人数
     */
    private Integer currentStudentCount;
    
    /**
     * 上课时间地点信息
     */
    private String scheduleInfo;
    
    /**
     * 状态（0:禁用, 1:启用）
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
     * 逻辑删除标识（0：已删除，1：未删除）
     */
    private Integer isDeleted;
}