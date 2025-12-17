package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业实体类
 * 对应数据库表: homework
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homework implements Serializable {
    
    /**
     * 作业ID
     */
    private Long id;
    
    /**
     * 作业标题
     */
    private String title;
    
    /**
     * 作业描述
     */
    private String description;
    
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
     * 班级ID集合（逗号分隔）
     */
    private String classIds;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 截止时间
     */
    private LocalDateTime deadline;
    
    /**
     * 附件URL（多个用逗号分隔）
     */
    private String attachmentUrls;
    
    /**
     * 状态（0:草稿, 1:已发布, 2:已结束）
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