package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 选课记录实体类
 * 对应数据库表: course_selection
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSelection implements Serializable {

    /**
     * 选课记录ID
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
     * 学期
     */
    private String semester;
    
    /**
     * 选课时间
     */
    private LocalDateTime selectionTime;
    
    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;
    
    /**
     * 选课状态（1:已选, 2:已取消, 3:已结课）
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