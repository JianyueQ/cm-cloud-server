package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考勤发起实体类
 * 对应数据库表: attendance_initiate
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceInitiate implements Serializable {

    /**
     * 考勤发起ID
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
     * 考勤开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 考勤结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 考勤类型（1:普通考勤, 2:请假, 3:补签）
     */
    private Integer attendanceType;
    
    /**
     * 经度
     */
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 考勤密码
     */
    private String signInPassword;

    /**
     * 签到方式
     */
    private Integer signInType;
    
    /**
     * 状态（1:进行中, 2:已结束）
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