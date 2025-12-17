package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程信息实体类
 * 对应数据库表: course
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {

    /**
     * 课程ID
     */
    private Long id;
    
    /**
     * 课程编码
     */
    private String courseCode;
    
    /**
     * 课程名称
     */
    private String name;
    
    /**
     * 课程描述
     */
    private String description;
    
    /**
     * 理论学时
     */
    private Integer theoryHours;
    
    /**
     * 实践学时
     */
    private Integer practiceHours;
    
    /**
     * 总学时
     */
    private Integer totalHours;
    
    /**
     * 课程类型（1:必修, 2:选修, 3:公选）
     */
    private Integer courseType;
    
    /**
     * 考核方式（1:考试, 2:考查）
     */
    private Integer assessmentType;
    
    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

    /**
     * 课程开始选择时间
     */
    private LocalDateTime selectionStartTime;

    /**
     * 课程结束选择时间
     */
    private LocalDateTime selectionEndTime;

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