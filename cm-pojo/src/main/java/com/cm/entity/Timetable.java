package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 课表信息实体类
 * 对应数据库表: timetable
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable implements Serializable {
    /**
     * 课表ID
     */
    private Long id;
    
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
     * 班级ID
     */
    private Long classId;
    
    /**
     * 班级名称（冗余）
     */
    private String className;
    
    /**
     * 星期（如周一、周二）
     */
    private String weekDays;
    
    /**
     * 开始时间
     */
    private LocalTime startTime;
    
    /**
     * 结束时间
     */
    private LocalTime endTime;
    
    /**
     * 上课地点
     */
    private String location;
    
    /**
     * 周次范围（如1-16周）
     */
    private String weekRange;
    
    /**
     * 学期
     */
    private String semester;
    
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