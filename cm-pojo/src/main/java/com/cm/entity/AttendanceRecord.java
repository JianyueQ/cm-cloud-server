package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考勤记录实体类
 * 对应数据库表: attendance_record
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecord implements Serializable {

    /**
     * 考勤记录ID
     */
    private Long id;
    
    /**
     * 考勤发起ID
     */
    private Long attendanceInitiateId;
    
    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 学号（冗余）
     */
    private String studentNo;
    
    /**
     * 学生姓名（冗余）
     */
    private String studentName;

    /**
     * 头像
     */
    private String avatar;

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
     * 签到时间
     */
    private LocalDateTime signInTime;
    
    /**
     * 签退时间
     */
    private LocalDateTime signOutTime;
    
    /**
     * 签到经度
     */
    private BigDecimal longitude;
    
    /**
     * 签到纬度
     */
    private BigDecimal latitude;

    /**
     * 签到密码
     */
    private String signInPassword;

    /**
     * 考勤状态（1:已签到, 2:迟到, 3:未签到, 4:请假）
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